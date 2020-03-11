package endpoints;

import databaseInit.Database;
import entities.*;
import websockets.NotificationSocket;
import websockets.SocketMessage;
import websockets.SocketMessageType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for handling the saving of a sessions {@link Order}
 * <p>
 * Spec:
 * POST - int: table_num
 */
public class SaveOrder extends HttpServlet {

    /**
     * Saves the sessions {@link Order} to the database
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = getOrder(req);
        if (order == null) {
            resp.sendError(500, "No Order exists for this session.");
            return;
        }
        if (order.getFoodItems().isEmpty()) {
            resp.sendError(400, "Order is empty!");
            return;
        }
        //table Num validation
        String tableNum = req.getParameter("table_num");
        int table;
        try {
            table = Integer.parseInt(tableNum);
        } catch (Exception ex) {
            resp.sendError(400, "invalid num");
            return;
        }

        order.setTimeOrdered(System.currentTimeMillis());
        //gets table from tables list, if cannot find table updates the list from database
        //if still no table it sends an error for invalid table num
        Table tableSeated = TableState.getTableByID(table);
        if (tableSeated == null && TableState.getTableList().size() == 0) {
            try {
                Database.TABLES.fetchTables();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            tableSeated = TableState.getTableByID(table);
        }
        if (tableSeated == null) {
            resp.sendError(400, "Invalid Table Num");
            return;

        } else {
            //Notification sent to waiters that order is placed and needs to be confirmed
            Notification n = new Notification(tableSeated, NotificationTypes.CONFIRM);
            //Checks if table has been assigned to a waiter, if it has sends a notification to waiter
            //If the table is not in any waiter list of tables table is put as need waiter.
            //todo do we need the table to hold a staff instance?
            //todo maybe need to assign a random water if waiter is not found rather than putting table back into looking for waiter.
            if (ActiveStaff.findTableWaiter(tableSeated) != null) {
                tableSeated.setWaiter(ActiveStaff.findTableWaiter(tableSeated));
            } else {
                Notification notif = new Notification(tableSeated, NotificationTypes.NEED);
                NotificationSocket.broadcastNotification(new SocketMessage(notif, SocketMessageType.CREATE));
                ActiveStaff.notifyAll(notif);
                TableState.addNeedWaiter(tableSeated);
                //ActiveStaff.notifyAll(n);
            }

            //Sends notification "order to be confirmed" to the waiter.
            ActiveStaff.addNotification(ActiveStaff.findTableWaiter(tableSeated), n);
            NotificationSocket.pushNotification(new SocketMessage(n, SocketMessageType.CREATE), ActiveStaff.findStaffForTable(tableSeated.tableNum));

            //order.setOrderConfirmed(System.currentTimeMillis() + 100);
            order.setTableNum(table);
            boolean success;
            try {
                success = Database.ORDERS.saveOrder(order);
                req.getSession().setAttribute("order", null);
            } catch (SQLException e) {
                resp.sendError(500, "Unable to save Order. Database error");
                e.printStackTrace();
                return;
            }
            if (!success) {
                resp.sendError(500, "Unable to save Order.");
                return;
            }
        }


    }

    /**
     * Gets the sessions {@link Order} returns null if no Order exists
     *
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The sessions Basket or null if none exists
     */
    private Order getOrder(HttpServletRequest req) {
        return (Order) req.getSession().getAttribute("order");
    }
}
