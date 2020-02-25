
package endpoints;

import databaseInit.Database;
import entities.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;
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
        Table t = null;
        Random rand = new Random();
        List<StaffInstance> active = ActiveStaff.getAllActiveStaff();

        if (active.size() == 0) {
            resp.sendError(500, "No current active staff.");
        }

        StaffInstance waiter = active.get(rand.nextInt(active.size()));

        try {
            t = Database.TABLES.getTableByID(getTable(req, resp));
        } catch (SQLException e) {
            resp.sendError(500, "Error getting Table from database.");
        }

        if (t != null && ActiveStaff.isActive(waiter.getStaffID())) {
            Notification n = new Notification(t, NotificationTypes.ASSIST);
            ActiveStaff.addNotification(waiter.getStaffID(), n);
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
