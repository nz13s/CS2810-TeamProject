package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import databaseInit.Database;
import entities.NotificationTypes;
import entities.Order;
import entities.StaffInstance;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import websockets.NotificationSocket;
import websockets.SocketMessage;
import websockets.SocketMessageType;

/**
 * Endpoint for the frontend to call to get the notifications for a sessions {@link StaffInstance}.
 *
 * <p>Spec:
 * GET - No params
 * DELETE - int: notificationID
 * DELETE - String: notificationType
 *
 * @author Cameron Jones
 */
public class StaffNotifications extends HttpServlet {

  private ObjectMapper om = new ObjectMapper();

  /**
   * Gets the notifications stored for the staff member.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    StaffInstance staff = getStaffMember(req);
    resp.reset();
    resp.setContentType("application/json");
    PrintWriter pw = resp.getWriter();
    pw.println(om.writeValueAsString(staff.getNotifications()));
    pw.flush();
  }

  /**
   * Removes the notification for the sessions {@link StaffInstance} specified by the user input.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int id;
    NotificationTypes type;
    StaffInstance staff = getStaffMember(req);
    try {
      id = Integer.parseInt(req.getParameter("notificationID"));
      type = staff.getNotificationByID(id).getType();
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid ID.");
      return;
    }
    if (type == NotificationTypes.READY) {
      Order o = null;
      try {
        o = getOrder(req);
      } catch (SQLException e) {
        resp.sendError(500, "Could not get Order.");
      }
      try {
        Database.ORDERS.updateOrderServedByID(Objects.requireNonNull(o).getOrderID());
      } catch (SQLException e) {
        resp.sendError(500, "Unable to update order.");
      } catch (NullPointerException d) {
        resp.sendError(500, "Could not get Order.");
      }
    }
    NotificationSocket.pushNotification(new SocketMessage(staff.getNotificationById(id),
            SocketMessageType.DELETE), staff);
    staff.removeNotificationByID(id);
  }

  /**
   * Gets the sessions {@link StaffInstance} associated and returns it.
   *
   * @param req The {@link HttpServletRequest} object that contains
   *            the request the client made of the servlet.
   * @return The StaffInstance associated with the session
   */
  private StaffInstance getStaffMember(HttpServletRequest req) {
    StaffInstance staff = (StaffInstance) req.getSession().getAttribute("StaffEntity");
    if (staff == null) {
      //staff = new StaffInstance("Bob");
      //TODO Throw error and remove create instance since only staff should be able to
      //get here
      //req.getSession().setAttribute("staffMember", staff);
      throw new IllegalStateException("No staff object found.");
    }
    return staff;
  }

  /**
   * Gets the orderID from frontend as parameter.
   *
   * @param req The {@link HttpServletRequest} object that contains
   *            the request the client made of the servlet
   * @return The sessions Basket or null if none exists
   */
  @Nonnull
  private Order getOrder(HttpServletRequest req) throws SQLException {
    int orderID = Integer.parseInt(req.getParameter("orderID"));
    Order order = Database.ORDERS.getOrderByID(orderID);
    assert order != null;
    return order;
  }
}

