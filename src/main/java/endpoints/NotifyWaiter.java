
package endpoints;

import databaseInit.Database;
import entities.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

public class NotifyWaiter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        Table table = null;
        try {
            table = Database.TABLES.getTableByID(getTable(req, resp));
        } catch (SQLException e) {
            resp.sendError(500, "Error getting TableID from database.");
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
                }
            }

            ActiveStaff.addNotification(table.getWaiter(), n);

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
