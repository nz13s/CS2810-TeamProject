
package endpoints;

import databaseInit.Database;
import entities.ActiveStaff;
import entities.Notification;
import entities.StaffInstance;
import entities.Table;

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
        String type = getType(req);

        if (t != null && type != null) {
            Notification n = new Notification(t, type);

            List<StaffInstance> active = ActiveStaff.getStaff();
            Random rand = new Random();
            StaffInstance waiter = active.get(rand.nextInt(active.size()));

            ActiveStaff.addNotification(waiter.getStaffID(), n);
        } else {
            resp.sendError(500, "Null value table or type");
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

    private String getType(HttpServletRequest req) {
        String type = null;
        type = req.getParameter("type");
        return type;
    }

}
