package endpoints;

import databaseInit.Database;
import entities.Notification;
import entities.NotificationTypes;
import entities.Order;
import entities.Table;
import websockets.NotificationSocket;
import websockets.SocketMessage;
import websockets.SocketMessageType;

import javax.annotation.Nullable;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Class for handling the updating of order states. {@link Order}
 *
 * @author Tony Delchev, Bhavik Narang
 */
public class OrderUpdate extends HttpServlet {

  private entities.CustomerNotifications cNotifications = new entities.CustomerNotifications();

  /**
   * Validates the orderID and depending on the State(0-3), calls update method
   * with Different parameters for each state. Displays adequate error if fail.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Order order;
    Order oldOrder;
    boolean success;
    int state = getState(req, resp);

    try {
      order = getOrder(req);
      oldOrder = order;
    } catch (SQLException e) {
      resp.sendError(500, "Could not get OrderID.");
      return;
    }

    if (order == null) {
      resp.sendError(400, "No Order exists for this orderID");
      return;
    }

    if (state > 3 || state < 0) {
      resp.sendError(400, "Unexpected State Value.");
      return;
    }
    //todo Tony Look at this method needs to be updated.
    try {
      success = Database.ORDERS.updateOrderState(order, state);
      order = getOrder(req);
      if (order != null) {
        NotificationSocket.broadcastNotification(new SocketMessage(order,
                SocketMessageType.UPDATE));
        if (state == 2) {
          Table orderTable = Database.TABLES.getTableByID(order.getTableNum(), false);
          if (orderTable == null) {
            resp.sendError(500, "Unable to update order.");
            return;
          }
          Notification nfReady = new Notification(orderTable, NotificationTypes.READY);
          NotificationSocket.broadcastNotification(new SocketMessage(nfReady,
                  SocketMessageType.CREATE));

          // If there is no Waiter assigned to a table and No Waiter has the table in their list,
          // table is assigned to random Waiter.
          if (orderTable.getWaiter() == null) {
            if (ActiveStaff.findTableWaiter(orderTable) != null) {
              orderTable.setWaiter(ActiveStaff.findTableWaiter(orderTable));
            } else {
              ActiveStaff.addTableToRandomStaff(orderTable);
            }
          }
          //Sends notification "order is ready" to the waiter.
          ActiveStaff.addNotification(orderTable.getWaiter(), nfReady);

          // add notification to the customer's order
          cNotifications.addNotification(nfReady);

        } else if (state == 0) {
          Table orderTable = Database.TABLES.getTableByID(order.getTableNum());
          Notification nfConfirmed = new Notification(orderTable, NotificationTypes.CONFIRMED);
          cNotifications.addNotification(nfConfirmed);
        } else if (state == 1) {
          Table orderTable = Database.TABLES.getTableByID(order.getTableNum());
          Notification nfPreparing = new Notification(orderTable, NotificationTypes.PREPARING);
          cNotifications.addNotification(nfPreparing);
        }
      } else {
        NotificationSocket.broadcastNotification(new SocketMessage(oldOrder,
                SocketMessageType.DELETE));
      }


    } catch (SQLException e) {
      resp.sendError(500, "Unable to update order.");
      return;
    }

    if (!success) {
      resp.sendError(500, "Unable to update order.");
      return;
    }
  }

  /**
   * Gets the orderID from frontend as parameter.
   *
   * @param req The {@link HttpServletRequest} object that contains
   *            the request the client made of the servlet.
   * @return The sessions Basket or null if none exists
   */

  @Nullable
  private Order getOrder(HttpServletRequest req) throws SQLException {
    int orderID = Integer.parseInt(req.getParameter("orderID"));
    List<Order> queue = OrdersToFrontend.getOrderQueue();
    return queue.stream().filter(item ->
            item.getOrderID() == orderID).findFirst().orElse(null);
  }

  /**
   * Gets the state int from frontend as parameter.
   *
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @return The state integer
   */
  private int getState(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int state = -1;
    try {
      state = Integer.parseInt(req.getParameter("state"));
      if (state < 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid State integer.");
    }
    return state;
  }
}




