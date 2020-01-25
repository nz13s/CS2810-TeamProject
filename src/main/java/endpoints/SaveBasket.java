package endpoints;

import databaseInit.Database;
import entities.Basket;
import entities.Order;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Class for handling modification and access to a sessions {@link Basket}
 */
@WebServlet("/save")
public class SaveBasket extends HttpServlet {

    /**
     * Saves the sessions {@link Basket} to the database
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Basket basket = getBasket(req);
        if (basket == null) {
            resp.sendError(500, "No order exists for this session.");
            return;
        }

        if (basket.getBasket().isEmpty()){
            resp.sendError(400, "Basket is empty!");
        }

        boolean allGood = true;
        //TODOne @Oliver please
        //saveBasket returns boolean
        Order order = new Order(System.currentTimeMillis(), 1); //todo patch tablenum through
        basket.getBasket().forEach(order::addFoodItem);

        boolean success = false;

        try {
            success = Database.ORDERS.saveOrder(order);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (!success) {
            resp.sendError(500, "Unable to save basket.");
        }
    }

    /**
     * Gets the sessions {@link Basket} returns null if no Basket exists
     *
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The sessions Basket or null if none exists
     */
    private Basket getBasket(HttpServletRequest req) {
        return (Basket) req.getSession().getAttribute("basket");
    }
}
