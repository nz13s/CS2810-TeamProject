package endpoints.restricted;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import endpoints.GlobalMethods;
import entities.StaffInstance;
import entities.Table;
import entities.TableState;
import entities.serialisers.TablesInfoSerialiser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Endpoint to frontend for table removing.
 *
 * @author Nick Bogachev
 */
public class TableManagement extends HttpServlet {
    private ObjectMapper mapper;

    public TableManagement() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule("Serialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(TableState.class, new TablesInfoSerialiser());
        mapper.registerModule(module);
    }

    /**
     * Method that converts the Table list of StaffInstance to JSON.
     *
     * @return JSON String with a list of tables.
     * @throws IOException exception
     */
    private String tableListToJSON() throws IOException {
        return mapper.writeValueAsString(StaffInstance.getTables());
    }

    /**
     * Method that GETs the StaffInstance tables to frontend.
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
            pw.println(this.tableListToJSON());
        } catch (IOException e) {
            pw.println(e.getMessage());
        }
        pw.flush();
    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Table table = null;
        StaffInstance staff;
        staff = getStaff(req);
        if (staff == null) {
            resp.sendError(500, "Invalid staff instance.");
        }
        boolean success = false;
        try {
            table = TableState.getTableByID(GlobalMethods.getTable(req, resp));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (table != null) {
            assert staff != null;
            TableState.removeNeedWaiter(table);
            staff.removeTable(table);
            success = true;
        }
        if (!success) {
            resp.sendError(500, "Failed to remove table from list.");
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
}
