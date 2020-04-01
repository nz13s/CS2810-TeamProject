package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Table;
import entities.TableState;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;

public class AllTables extends HttpServlet {

    ObjectMapper om;

    public AllTables() {
        om = new ObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            TableState.updateTables();
        } catch (SQLException e) {
            resp.sendError(500, "SQL Error: Failed to update tables.");
            return;
        }
        List<Table> tables = null;
        try {
            tables = TableState.getTableList();
        } catch (SQLException e) {
            resp.sendError(500, "SQL Error: Failed to get table list.");
            return;
        }
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        pw.println(om.writeValueAsString(tables));
        pw.flush();
    }
}
