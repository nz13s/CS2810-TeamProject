package endpoints;

import databaseInit.Database;
import entities.*;
import websockets.NotificationSocket;
import websockets.SocketMessage;
import websockets.SocketMessageType;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
     * @param req  The {@link HttpServletRequest} object that contains the request
     *             the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response
     *             the servlet returns to the client
     * @throws IOException If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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
        Table tableSeated = null;
        try {
            tableSeated = TableState.getTableByID(table);
        } catch (SQLException e) {
            resp.sendError(400, "Table not found, refresh table list");
            return;
        }

        if (tableSeated == null) {
            resp.sendError(400, "Invalid Table Num");
        } else {
            //Notification sent to waiters that order is placed and needs to be confirmed

            order.setTableNum(table);
            int success;
            try {
                success = Database.ORDERS.saveOrder(order);
                order.setOrderID(success);
                Notification n = new Notification(tableSeated, NotificationTypes.CONFIRM);
                n.setExtraData(order);
                //Checks if table has been assigned to a waiter, if it has sends a notification to waiter
                //If the table is not in any waiter list of tables table is put as need waiter.
                //todo do we need the table to hold a staff instance?
                //todo maybe need to assign a random water if waiter is not found rather than putting table into looking for staff

                if (ActiveStaff.findTableWaiter(tableSeated) != null) {
                    ActiveStaff.addTableToRandomStaff(tableSeated);
                } else {
                    ActiveStaff.addNotification(ActiveStaff.findTableWaiter(tableSeated), n);
                    NotificationSocket.pushNotification(
                            new SocketMessage(n, SocketMessageType.CREATE), ActiveStaff.findStaffForTable(tableSeated.tableNum));
                    //     tableSeated.setWaiter(ActiveStaff.findTableWaiter(tableSeated));
                    //Notification notif = new Notification(tableSeated, NotificationTypes.NEED);
                    // NotificationSocket.broadcastNotification(new SocketMessage(notif, SocketMessageType.CREATE));
                    // ActiveStaff.notifyAll(notif);
                    //  TableState.addNeedWaiter(tableSeated);
                    //ActiveStaff.notifyAll(n);
                }

                //Sends notification "order to be confirmed" to the waiter.
                //Bug if no waiter is assigned will be a nullpointer need to decide wether to put a random waiter
                //   ActiveStaff.addNotification(ActiveStaff.findTableWaiter(tableSeated), n);
                //   NotificationSocket.pushNotification(new SocketMessage(n, SocketMessageType.CREATE), ActiveStaff.findStaffForTable(tableSeated.tableNum));

                req.getSession().setAttribute("order", null);

                PrintWriter pw = resp.getWriter();
                pw.print(order.getOrderID());
                pw.flush();

            } catch (SQLException e) {
                resp.sendError(500, "Unable to save Order. Database error");
                e.printStackTrace();
                return;
            }
            if (success < 0) {
                resp.sendError(500, "Unable to save Order.");
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
