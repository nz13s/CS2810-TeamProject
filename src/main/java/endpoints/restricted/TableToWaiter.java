package endpoints.restricted;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import endpoints.GlobalMethods;
import entities.*;
import entities.serialisers.TablesInfoSerialiser;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import websockets.NotificationSocket;
import websockets.SocketMessage;
import websockets.SocketMessageType;

/**
 * The class to assign a Table to waiter.
 *
 * @author Nick Bogachev, Jatin Khatra, Tony Delchev
 */
public class TableToWaiter extends HttpServlet {
  private ObjectMapper mapper;
  private CustomerNotifications cNotifications;

  /**
   * Instantiates a new Table to waiter.
   */
  public TableToWaiter() {
    mapper = new ObjectMapper();
    mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    SimpleModule module = new SimpleModule("Serialiser",
            new Version(1, 0, 0, null,
                    null, null));
    module.addSerializer(TableState.class, new TablesInfoSerialiser());
    mapper.registerModule(module);

    cNotifications = new CustomerNotifications();
  }

  /**
   * Method that converts the TableState into JSON.
   *
   * @return JSON String representing the state of all tables.
   * @throws IOException exception
   */
  public String tablesToJson() throws IOException {
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
      pw.println(this.tablesToJson());
    } catch (IOException e) {
      pw.println(e.getMessage());
    }
    pw.flush();
  }

  /**
   * The Post method, Validates frontend input, gets Table from Database,
   * Sends off a notification to all active staff and adds the table to NeedWaiter list.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
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
      table = TableState.getTableByID(GlobalMethods.getTable(req, resp));
    } catch (SQLException e) {
      resp.sendError(400, "Invalid tableNum");
    }

    if (table != null) {
      assert staff != null;
      try {
        success = ActiveStaff.addTableToStaff(table, staff);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      Notification notif = new Notification(table, NotificationTypes.ASSIGN);
      ActiveStaff.addNotification(staff, notif);
      NotificationSocket.pushNotification(new SocketMessage(notif, SocketMessageType.CREATE),
              staff);
      TableState.addOccupied(table);
      TableState.removeNeedWaiter(table);

      //after the waiter has been assigned to a table, all previous notifications for the table
      // need to be removed.
      cNotifications.removeNotificationsByTableNum(table.getTableNum());

    }
    if (!success) {
      resp.sendError(500, "Failed to waiters notification");
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

