package endpoints;

import databaseInit.Database;
import entities.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;


/**
 * Class for handling the updating of order sessions {@link Order}
 */
@WebServlet("/update")
public class OrderUpdate extends HttpServlet {

    /**
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = null;
        boolean success = false;

        int state = getState(req);


        if (order == null) {
            resp.sendError(500, "No Order exists for this session.");
            return;
        }
        if (order.getFoodItems().isEmpty()) {
            resp.sendError(400, "Order is empty!");
            return;
        }

        try {
            order = Database.ORDERS.getOrderByID(getOrder(req));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        switch (state) {
            case 1:
                try {
                    success = Database.ORDERS.updateOrderState(System.currentTimeMillis(), 0l, 0l, order);

                } catch (SQLException e) {
                    resp.sendError(500, "Unable to update Order. Database error");
                    e.printStackTrace();
                }
            case 2:
                try {
                    success = Database.ORDERS.updateOrderState(order.getOrderPreparing(), System.currentTimeMillis(), 0l, order);
                    resp.sendError(500, "Unable to update Order. Database error");

                } catch (SQLException e) {
                    resp.sendError(500, "Unable to update Order. Database error");
                    e.printStackTrace();
                }
            case 3:
                try {
                    success = Database.ORDERS.updateOrderState(order.getOrderPreparing(), order.getOrderReady(), System.currentTimeMillis(), order);

                } catch (SQLException e) {
                    resp.sendError(500, "Unable to update Order. Database error");
                    e.printStackTrace();
                }


        }

        if (!success) {
            resp.sendError(500, "Unable to save Order.");
        }
        return;

    }

    /**
     * Gets the sessions {@link Order} returns null if no Order exists
     *
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The sessions Basket or null if none exists
     */
    private int getOrder(HttpServletRequest req) {
        return (int) req.getSession().getAttribute("orderID");
    }

    private int getState(HttpServletRequest req) {
        return (int) req.getSession().getAttribute("orderState");
    }
}




