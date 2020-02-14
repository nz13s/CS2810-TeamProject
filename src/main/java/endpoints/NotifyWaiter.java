
package endpoints;

import databaseInit.Database;
import entities.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

//TODO -- Tony Validation / Documentation and finishing off class.
public class NotifyWaiter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Table t = null;
        try {
            t = Database.TABLES.getTableByID(getTable(req, resp));
        } catch (SQLException e) {
            resp.sendError(500, "Error getting TableID from database.");
        }

        if (t != null) {
            Notification n = new Notification(t, NotificationTypes.ASSIST);
            List<StaffInstance> active = ActiveStaff.getAllActiveStaff();
            Random rand = new Random();

            StaffInstance waiter = active.get(rand.nextInt(active.size()));
            ActiveStaff.addNotification(waiter.getStaffID(), n);
        } else {
            resp.sendError(500, "Null value tableID");
        }

    }
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
