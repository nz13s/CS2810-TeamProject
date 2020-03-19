package endpoints.restricted;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import databaseInit.Database;
import endpoints.GlobalMethods;
import entities.*;
import entities.serialisers.TablesInfoSerialiser;
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
 * Class that converts all tables from the database to JSON, and allows it to be acquired by the frontend.
 *
 * @author Jatin
 */

public class TableInfo extends HttpServlet {

    private ObjectMapper mapper;

    /**
     * Constructor that initialises the mapper attribute.
     */

    public TableInfo() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule("Serialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(TableState.class, new TablesInfoSerialiser());
        mapper.registerModule(module);
    }

    /**
     * Method that converts the TableState into JSON.
     *
     * @return JSON String representing the state of all tables.
     * @throws SQLException
     * @throws IOException
     */

    public String tablesToJSON(int refresh, HttpServletResponse resp) throws SQLException, IOException {
        //TODO The initial load of tables from DB into the list.

        if (TableState.getTableList().isEmpty()) {
            Database.TABLES.fetchTables();
        }
        if (refresh == 1) {
            Database.TABLES.fetchTables();
        }

        return mapper.writeValueAsString(TableState.getTableUnOccupied());
    }

    /**
     * Method that GETs the JSON object.
     *
     * @param req  server request.
     * @param resp server response.
     * @throws IOException
     */

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int refresh = getRefresh(req, resp);
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        try {
            pw.println(this.tablesToJSON(refresh, resp));
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
        boolean success = false;

        try {
            table = TableState.getTableByID(GlobalMethods.getTable(req, resp));
        } catch (SQLException e) {
            resp.sendError(400, "Invalid tableNum");
        }
        if (ActiveStaff.hasWaiter(table)) {
            resp.sendError(500, "Table is already occupied");
        } else {
            if (table != null) {
                TableState.addNeedWaiter(table);
                Notification notif = new Notification(table, NotificationTypes.NEED);
                NotificationSocket.broadcastNotification(new SocketMessage(notif, SocketMessageType.CREATE));
                success = ActiveStaff.notifyAll(notif);
            }

            if (!success) {
                resp.sendError(500, "Failed to send waiters notification");
            }
        }
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

    private int getRefresh(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int refresh = -1;
        try {
            refresh = Integer.parseInt(req.getParameter("refresh"));
            if (refresh < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid refresh state.");
        }
        return refresh;
    }
}
