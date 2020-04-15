package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import endpoints.GlobalMethods;
import entities.*;
import websockets.NotificationSocket;
import websockets.SocketMessage;
import websockets.SocketMessageType;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * The class to set up a waiter panel with tables.
 *
 * @author Tony Delchev
 */
public class WaiterPanel extends HttpServlet {
  ObjectMapper om;

  /**
   * Constructor that initialises the mapper attribute.
   */
  public WaiterPanel() {
    om = new ObjectMapper();
  }

  /**
   * On GET gets Staff Instance Managed tables list.
   * Converts the table list to JSON. Sends to front end.
   *
   * @param req  server request.
   * @param resp server response.
   * @throws IOException
   */
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    StaffInstance staff;
    staff = getStaff(req);
    if (staff == null) {
      resp.sendError(500, "Invalid staff instance.");
    }
    List<Table> tables = null;
    tables = staff.getTables();
    resp.setContentType("application/json");
    PrintWriter pw = resp.getWriter();
    pw.println(om.writeValueAsString(tables));
    pw.flush();
  }

  /**
   * On DELETE removes table from staff managed tables,
   * Table is now UnOccupied and can be assigned again.
   *
   * @param req  server request.
   * @param resp server response.
   * @throws IOException
   */
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Table table = null;
    StaffInstance staff;
    staff = getStaff(req);
    boolean success = false;
    try {
      table = TableState.getTableByID(GlobalMethods.getTable(req, resp));
    } catch (SQLException e) {
      resp.sendError(400, "Invalid tableNum.");
    }

    try {
      success = ActiveStaff.removeTableFromStaff(table, staff);
    } catch (SQLException e) {
      resp.sendError(500, "Unable to remove table from staff.");
    }
    if (!success) {
      resp.sendError(500, "Unable to remove table from staff.");
    }

  }

  /**
   * On POST Puts table for re-assigning, removes from Staff.
   * Sends Notification to all staff that table needs waiter.
   *
   * @param req  server request.
   * @param resp server response.
   * @throws IOException
   */
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Table table = null;
    StaffInstance staff;
    staff = getStaff(req);
    try {
      table = TableState.getTableByID(GlobalMethods.getTable(req, resp));
    } catch (SQLException e) {
      resp.sendError(400, "Invalid tableNum.");
    }
    staff.removeTable(table);
    TableState.addNeedWaiter(table);
    Notification notif = new Notification(table, NotificationTypes.NEED);
    NotificationSocket.broadcastNotification(new SocketMessage(notif, SocketMessageType.CREATE));

    boolean success = ActiveStaff.notifyAll(notif);
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


}
