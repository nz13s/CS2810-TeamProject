
package endpoints;

import databaseInit.Database;
import entities.ActiveStaff;
import entities.Notification;
import entities.Table;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

//TODO -- Tony Validation / Documentation and finishing off class.
@WebServlet("/notification") //todo -- what goes in brackets?
public class NotifyWaiter extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Table t = null;
        try {
            t = Database.TABLES.getTableByID(getTable(req, resp));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        String type = getType(req);

        if (t != null && type != null) {
            Notification n = new Notification(t, type);
            ActiveStaff.addNotification(2, n);
        } else {
            resp.sendError(500, "Something Went Wrong");
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
            resp.sendError(400, "Invalid State integer.");
        }
        return tableID;
    }

    private String getType(HttpServletRequest req) {
        String type = null;
        type = req.getParameter("type");
        return type;
    }

}
