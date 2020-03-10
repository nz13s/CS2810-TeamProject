
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
import java.sql.SQLException;




/**
 * Class for customer to call for assistance from waiter, adds notification to waiter.
 *
 * @author Tony
 */

public class NotifyWaiter extends HttpServlet {
    /**
     * Validates that there is Active staff and that there is a table for this TableID.
     * Creates a new notification type ASSIST to a random Active Staff member from the ActiveStaff List.
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws IOException If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {

        Table table = null;
        try {
            table = Database.TABLES.getTableByID(getTable(req, resp));
        } catch (SQLException e) {
            resp.sendError(500, "Error getting Table from database.");
        }


        if (table != null) {
            Notification n = new Notification(table, NotificationTypes.ASSIST);
            if (table.getWaiter() == null) {
                if (ActiveStaff.findTableWaiter(table) != null) {
                    table.setWaiter(ActiveStaff.findTableWaiter(table));
                } else {
                    Notification notif = new Notification(table, NotificationTypes.NEED);
                    ActiveStaff.notifyAll(notif);
                    TableState.addNeedWaiter(table);
                    ActiveStaff.notifyAll(n);
                    NotificationSocket.broadcastNotification(new SocketMessage(n, SocketMessageType.CREATE));
                }
            }
            ActiveStaff.addNotification(table.getWaiter(), n);

        } else {
            resp.sendError(500, "Null value Table.");
        }
    }


    /**
     * Gets the tableID int from frontend as parameter.
     *
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The table ID
     */

    private int getTable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int tableID = -1;
        try {
            tableID = Integer.parseInt(req.getParameter("tableID"));
            if (tableID < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid tableID.");
        }
        return tableID;
    }
}
