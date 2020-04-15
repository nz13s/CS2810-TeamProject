package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Table;
import entities.TableState;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * AllTables class for getting all tables.
 *
 * @author Oliver Graham
 */

public class AllTables extends HttpServlet {

  ObjectMapper om;

  public AllTables() {
    om = new ObjectMapper();
  }

  /**
   * Gets all the tables as a list of type Table.
   * Prints the list of tables as String.
   *
   * @param req  servlet request
   * @param resp servlet response
   * @throws IOException if input/output error occurs
   */

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    try {
      TableState.updateTables();
    } catch (SQLException e) {
      resp.sendError(500, "SQL Error: Failed to update tables.");
      return;
    }
    List<Table> tables;
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
