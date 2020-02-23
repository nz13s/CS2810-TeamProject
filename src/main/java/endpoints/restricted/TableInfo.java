package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import databaseInit.Database;
import entities.ActiveStaff;
import entities.Table;
import entities.TableState;

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
    }

    /**
     * Method that converts the TableState into JSON.
     *
     * @return JSON String representing the state of all tables.
     * @throws SQLException
     * @throws IOException
     */

    public String tablesToJSON() throws SQLException, IOException {

        TableState tableState = Database.TABLES.fetchTables();
        return mapper.writeValueAsString(tableState);
    }

    /**
     * Method that GETs the JSON object.
     *
     * @param req  server request.
     * @param resp server response.
     * @throws IOException
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
     * Method that updates the notification list via a POST method from frontend.
     *
     * @param req  server request.
     * @param resp server response.
     * @throws IOException
     */

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Table table = null;
        boolean success = false;

        try {
            table = Database.TABLES.getTableByID(getTable(req, resp));
        } catch (SQLException e) {
            resp.sendError(400, "Invalid tableNum");
        }
        if (table != null) {
            success = ActiveStaff.addTableToStaff(table);
        }
        if (!success) {
            resp.sendError(500, "Failed to add table to waiter");
        }
    }

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
