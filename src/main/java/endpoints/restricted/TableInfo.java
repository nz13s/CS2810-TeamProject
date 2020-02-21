package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import databaseInit.Database;
import entities.StaffInstance;
import entities.Table;
import entities.TableState;
import notificationLogic.TableNotificationToWaiters;

import javax.servlet.annotation.WebServlet;
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

@WebServlet("/tables")
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
     * @param req server request.
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
     * @param req server request.
     * @param resp server response.
     * @throws IOException
     */

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int tableNum = Integer.parseInt(req.getParameter("tableNum"));
        StaffInstance staff = (StaffInstance) req.getSession().getAttribute("staffEntity");
        int staffID = staff.getStaffID();
        Table table = null;
        try {
            table = Database.TABLES.getTableByID(tableNum);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        TableNotificationToWaiters notif = new TableNotificationToWaiters();
        notif.addTableToStaff(table,staffID);
    }

}
