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
        if (basket != null) {
            boolean allGood = true;
            //TODO @Oliver please
            //saveBasket returns boolean
            Database.ORDERS.saveOrder((Order) basket);
            if (!allGood/*DB.saveBasket(basket)*/) {
                resp.sendError(500, "Unable to save basket.");
            }
        } else {
            resp.sendError(500, "No order exists for this session.");
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
