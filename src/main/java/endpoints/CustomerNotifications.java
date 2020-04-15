package endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Notification;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Endpoint that handles notifications related to customer's order tracking.
 *
 * @author Jatin Khatra
 */
public class CustomerNotifications extends HttpServlet {
  private ObjectMapper mapper;
  private entities.CustomerNotifications cNotifications;

  /**
   * Initialises attributes.
   */
  public CustomerNotifications() {
    mapper = new ObjectMapper();
    cNotifications = new entities.CustomerNotifications();
  }

  /**
   * GETs all notifications based on a given tableNum.
   *
   * @param req  servlet request.
   * @param resp servlet response.
   * @throws IOException if input/output error occurs.
   */

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    ArrayList<Notification> list = getNotificationsByTableNum(req, resp);
    resp.setContentType("application/json");
    PrintWriter pw = resp.getWriter();
    pw.println(mapper.writeValueAsString(list));
    pw.flush();
  }

  /**
   * DELETEs a customer notification based on a given notificationID.
   *
   * @param req  servlet request.
   * @param resp servlet response
   * @throws IOException if input/output error occurs.
   */
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int notificationID = -1;

    try {
      notificationID = Integer.parseInt(req.getParameter("notificationID"));
      if (notificationID < 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid ID entered.");
    }

    cNotifications.removeNotificationByID(notificationID);
  }

  /**
   * Method that gets all notifications for a given table number, and adds it to a list.
   *
   * @param req  servlet request.
   * @param resp servlet response.
   * @return list of all notifications for a given table number.
   * @throws IOException if input/output error occurs.
   */

  private ArrayList<Notification> getNotificationsByTableNum(HttpServletRequest req,
                                                             HttpServletResponse resp)
          throws IOException {
    int tableNum = -1;

    try {
      tableNum = Integer.parseInt(req.getParameter("tableNum"));
      if (tableNum < 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid table number.");
    }

    ArrayList<Notification> tableNotifications = new ArrayList<>();
    ArrayList<Notification> allCNotifications = cNotifications.getNotifications();

    for (Notification notification : allCNotifications) {
      if (notification.getTable().getTableNum() == tableNum) {
        tableNotifications.add(notification);
      }
    }

    return tableNotifications;
  }
}
