package endpoints.restricted;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import entities.Item;
import entities.Order;
import entities.serialisers.OrderSerialiser;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import javax.annotation.Nonnull;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Class for handling modification and access to a sessions {@link Order}.
 *
 * <p>Spec:
 * GET - No params
 * POST - int: item, int: count
 * DELETE - int: item, int: count
 *
 * @author Oliver Graham, Cameron Jones
 */
public class AddToOrder extends HttpServlet {
  private ObjectMapper om = new ObjectMapper();

  /**
   * Configures the {@link ObjectMapper} before being used to serialise the {@link Order} for
   * the frontend, uses the {@link OrderSerialiser}.
   */
  @Override
  public void init() {
    om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    SimpleModule module =
            new SimpleModule("OrderSerialiser", new Version(1, 0, 0, null, null, null));
    module.addSerializer(Order.class, new OrderSerialiser());
    om.registerModule(module);
  }

  /**
   * Returns the sessions {@link Order}.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    Order order = getOrder(req);
    resp.reset();
    resp.setContentType("application/json");
    PrintWriter pw = resp.getWriter();
    pw.println(om.writeValueAsString(order));
    pw.flush();
  }

  /**
   * Adds the specified amount of the specified {@link Item} from the sessions {@link Order}.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int[] idAmount;
    try {
      idAmount = getIdAmount(req, resp);
    } catch (NumberFormatException e) {
      return;
    }
    Order order = getOrder(req);
    try {
      order.addFoodItem(idAmount[0], idAmount[1]);
    } catch (SQLException e) {
      resp.sendError(400, "Item not found");
    }
  }

  /**
   * Deletes the specified amount of the specified {@link Item} from the sessions {@link Order}.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs.
   */
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int[] idAmount;
    try {
      idAmount = getIdAmount(req, resp);
    } catch (NumberFormatException e) {
      return;
    }
    Order order = getOrder(req);
    order.removeFoodItem(idAmount[0], idAmount[1]);
  }

  /**
   * Gets the ID and amount of the {@link Item} being sent.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @return A 2D array with the ID and the amount of the Item being referenced
   * @throws NumberFormatException If the inputted values are not integers or less than 1
   * @throws IOException           If an input or output exception occurs
   */
  private int[] getIdAmount(HttpServletRequest req, HttpServletResponse resp) throws
          NumberFormatException, IOException {
    int id;
    try {
      id = Integer.parseInt(req.getParameter("item"));
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid ID.");
      throw new NumberFormatException();
    }
    int amount;
    try {
      amount = Integer.parseInt(req.getParameter("count"));
      if (amount < 1) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid amount.");
      throw new NumberFormatException();
    }
    return new int[]{id, amount};
  }

  /**
   * Gets the sessions {@link Order}, if no Order is found an empty one is created.
   *
   * @param req The {@link HttpServletRequest} object that contains the request
   *            the client made of the servlet.
   * @return The sessions Order or a newly created empty one.
   */
  @Nonnull
  private Order getOrder(HttpServletRequest req) {
    Order order = (Order) req.getSession().getAttribute("order");
    if (order == null) {
      order = new Order();
      req.getSession().setAttribute("order", order);
    }
    return order;
  }
}
