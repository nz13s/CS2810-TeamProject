package endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import entities.Basket;
import entities.Item;
import entities.serialisers.BasketSerialiser;

import javax.annotation.Nonnull;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Class for handling modification and access to a sessions {@link Basket}
 */
@WebServlet("/baskets")
public class AddToBasket extends HttpServlet {

    //https://www.baeldung.com/jackson-object-mapper-tutorial
    private ObjectMapper om = new ObjectMapper();

    /**
     * Configures the {@link ObjectMapper} before being used to serialise the {@link Basket} for the frontend
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module =
                new SimpleModule("BasketSerialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Basket.class, new BasketSerialiser());
        om.registerModule(module);
    }

    /**
     * Returns the sessions {@link Basket}
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException If an input or output exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Basket basket = getBasket(req);
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        pw.println(om.writeValueAsString(basket));
        pw.flush();
    }

    /**
     * Adds the specified amount of the specified {@link Item} from the sessions {@link Basket}
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int[] IDAmount;
        try {
            IDAmount = getIDAmount(req, resp);
        } catch (NumberFormatException e) {
            return;
        }
        Basket basket = getBasket(req);
        try {
            basket.addToBasket(IDAmount[0], IDAmount[1]);
        } catch (SQLException e) {
            resp.sendError(400, "Item not found");
        }
    }

    /**
     * Deletes the specified amount of the specified {@link Item} from the sessions {@link Basket}
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException If an input or output exception occurs
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int[] IDAmount;
        try {
            IDAmount = getIDAmount(req, resp);
        } catch (NumberFormatException e) {
            return;
        }
        Basket basket = getBasket(req);
        basket.removeFromBasket(IDAmount[0], IDAmount[1]);
    }

    /**
     * Gets the ID and amount of the {@link Item} being sent
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @return A 2D array with the ID and the amount of the Item being referenced
     * @throws NumberFormatException If the inputted values are not integers or less than 1
     * @throws IOException If an input or output exception occurs
     */
    private int[] getIDAmount(HttpServletRequest req, HttpServletResponse resp) throws NumberFormatException, IOException {
        int ID;
        try {
            ID = Integer.parseInt(req.getParameter("item"));
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
        return new int[]{ID, amount};
    }

    /**
     * Gets the sessions {@link Basket}, if no Basket is found an empty one is created
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The sessions Basket or a newly created empty one
     */
    @Nonnull
    private Basket getBasket(HttpServletRequest req) {
        Basket basket = (Basket) req.getSession().getAttribute("basket");
        if (basket == null) {
            basket = new Basket();
            req.getSession().setAttribute("basket", basket);
        }
        return basket;
    }
}
