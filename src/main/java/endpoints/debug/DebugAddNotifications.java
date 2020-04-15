package endpoints.debug;

import entities.*;
import java.io.IOException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import websockets.NotificationSocket;
import websockets.SocketMessage;
import websockets.SocketMessageType;

/**
 * Endpoint for the frontend to call to add notifications to the active {@link StaffInstance}.
 * Spec:
 * POST - int: staffID, String: message
 *
 * @author Cameron Jones
 */
public class DebugAddNotifications extends HttpServlet {

  /**
   * Sets a new {@link Notification} for a {@link StaffInstance} by
   * their ID if they are in the {@link ActiveStaff}.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int id;
    try {
      id = Integer.parseInt(req.getParameter("staffID"));
      if (id < 0) {
        resp.sendError(400, "Invalid ID.");
        return;
      }
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid ID.");
      return;
    }
    String message = req.getParameter("message");
    if (message == null) {
      resp.sendError(400, "No message for Notification.");
      return;
    }
    Notification toSend = new Notification(message);
    ActiveStaff.addNotification(id, toSend);
    NotificationSocket.pushNotification(new SocketMessage(toSend, SocketMessageType.CREATE),
            ActiveStaff.getStaffByID(id));
  }
}
