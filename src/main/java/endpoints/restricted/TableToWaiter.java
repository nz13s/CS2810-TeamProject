package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import databaseInit.Database;
import entities.*;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * The type Table to waiter.
 */
public class TableToWaiter extends HttpServlet {
    private ObjectMapper mapper;

    /**
     * Instantiates a new Table to waiter.
     */
    public TableToWaiter() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    /**
     * Method that converts the TableState into JSON.
     *
     * @return JSON String representing the state of all tables.
     * @throws SQLException exception
     * @throws IOException  exception
     */
    public String tablesToJSON() throws SQLException, IOException {
        //TODO The initial load of tables from DB into the list.
        return mapper.writeValueAsString(TableState.getNeedWaiter());
    }

    /**
     * Method that GETs the JSON object.
     *
     * @param req  server request.
     * @param resp server response.
     * @throws IOException exception
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        try {
            pw.println(this.tablesToJSON());
        } catch (SQLException e) {
            pw.println(e.getMessage());
        }
        pw.flush();
    }

    /**
     * The Post method, Validates frontend input, gets Table from Database,
     * Sends off a notification to all active staff and adds the table to NeedWaiter list.
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws IOException If an input or output exception occurs
     */

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Table table = null;
        StaffInstance staff;
        staff = getStaff(req);
        if (staff == null) {
            resp.sendError(500, "Invalid staff instance.");
        }
        boolean success = false;

        try {
            table = Database.TABLES.getTableByID(getTable(req, resp));
        } catch (SQLException e) {
            resp.sendError(400, "Invalid tableNum");
        }
        if (table != null) {
            assert staff != null;
            success = ActiveStaff.addTableToStaff(table, staff);
            Notification n = new Notification(table, NotificationTypes.ASSIGN);
            ActiveStaff.addNotification(staff, n);
            TableState.removeNeedWaiter(table);
        }
        if (!success) {
            resp.sendError(500, "Failed to send waiters notification");
        }
    }

    /**
     * The method to get a staff entity to be used in the post method.
     *
     * @param req Http Request
     * @return a staff entity
     */
    private StaffInstance getStaff(HttpServletRequest req) {
        return (StaffInstance) req.getSession().getAttribute("StaffEntity");
    }

    /**
     * Gets tableNum as Parameter, parse to Integer and send an error for any exceptions.
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws IOException If an input or output exception occurs
     */
    private int getTable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int table = -1;
        try {
            table = Integer.parseInt(req.getParameter("tableNum"));
            if (table < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid tableNum integer.");
        }
        return table;
    }
}

