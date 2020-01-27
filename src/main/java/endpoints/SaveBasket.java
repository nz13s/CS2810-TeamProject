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
 * Class for handling the saving of a sessions {@link Order}
 */
@WebServlet("/save")
public class SaveBasket extends HttpServlet {

    /**
     * Saves the sessions {@link Order} to the database
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Order order = getOrder(req);
        if (order == null) {
            resp.sendError(500, "No Order exists for this session.");
            return;
        }
        if (order.getFoodItems().isEmpty()) {
            resp.sendError(400, "Order is empty!");
            return;
        }
        //TODOne @Oliver please
        order.setTimeOrdered(System.currentTimeMillis());
        order.setTableNum(1);//todo patch tablenum through
        order.getFoodItems().forEach(order::addFoodItem);
        boolean success = false;
        try {
            success = Database.ORDERS.saveOrder(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (!success) {
            resp.sendError(500, "Unable to save Order.");
        }
    }

    /**
     * Gets the sessions {@link Order} returns null if no Order exists
     *
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The sessions Basket or null if none exists
     */
    private Order getOrder(HttpServletRequest req) {
        return (Order) req.getSession().getAttribute("order");
    }
}
