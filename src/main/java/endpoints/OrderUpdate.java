package endpoints;

import databaseInit.Database;
import entities.Order;

import javax.annotation.Nonnull;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Class for handling the updating of order states. {@link Order}
 *
 * @author Tony Delchev
 */
public class OrderUpdate extends HttpServlet {

    /**
     * Validates the orderID and depending on the State(0-3), calls update method
     * with Different parameters for each state. Displays adequate error if fail.
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws IOException If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Order order = null;
        boolean success = false;
        int state = getState(req, resp);

        try {
            order = getOrder(req);
        } catch (SQLException e) {
            resp.sendError(500, "Could not get OrderID.");
        }

        if (order == null) {
            resp.sendError(400, "No Order exists for this orderID");
            return;
        }

        if (state > 3 || state < 0) {
            resp.sendError(400, "Unexpected State Value.");
        }

        try {
            success = Database.ORDERS.updateOrderState(order, state);
        } catch (SQLException e) {
            resp.sendError(500, "Unable to update order.");
        }

        if (!success) {
            resp.sendError(500, "Unable to update order.");
        }
    }

    /**
     * Gets the orderID from frontend as parameter.
     *
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The sessions Basket or null if none exists
     */

    @Nonnull
    private Order getOrder(HttpServletRequest req) throws SQLException {
        int orderID = Integer.parseInt(req.getParameter("orderID"));
        Order order = Database.ORDERS.getOrderByID(orderID);
        assert order != null;
        return order;
    }

    /**
     * Gets the state int from frontend as parameter.
     *
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
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



