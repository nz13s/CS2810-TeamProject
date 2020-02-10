package endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import databaseInit.Database;
import entities.Order;
import entities.serialisers.OrderSerialiser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet("/staff/checkorder")
public class AcceptOrder extends HttpServlet {

    private ObjectMapper om = new ObjectMapper();

    /**
     * Configures the {@link ObjectMapper} before being used to serialise the {@link Order} for the frontend, uses the {@link OrderSerialiser}
     *
     * @throws ServletException
     */
    @Override
    public void init() throws ServletException {
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module =
                new SimpleModule("OrderSerialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Order.class, new OrderSerialiser());
        om.registerModule(module);
    }

    /**
     * Returns the {@link Order} requested by the front end, specified by its orderID
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderID;
        try {
            orderID = getOrderID(req, resp);
        } catch (NumberFormatException e) {
            return;
        }
        Order order = null;
        try {
            order = Database.ORDERS.getOrderByID(orderID);
        } catch (SQLException e) {
            resp.sendError(500, "Unable to retrieve order from database.");
        }
        if (order == null) {
            resp.sendError(400, "No order for that ID.");
        }
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        pw.println(om.writeValueAsString(order));
        pw.flush();
    }

    /**
     * Sets the specified {@link Order} to be confirmed at teh current time
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int orderID;
        try {
            orderID = getOrderID(req, resp);
        } catch (NumberFormatException e) {
            return;
        }
        //TODO Need this method please
        /*try {
            //Database.ORDERS.confirmOrder(orderID, System.currentTimeMillis());
        } catch (SQLException e) {
            resp.sendError(500, "Unable to confirm order in Database.");
        }*/
    }

    /**
     * Gets the int Order ID passed in the request from the frontend
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @return The order ID of the order, as stored in the database, for the staff member to check before it is validated
     * @throws IOException           IOException If an input or output exception occurs
     * @throws NumberFormatException Thrown when an invalid number is passed by the frontend
     */
    private int getOrderID(HttpServletRequest req, HttpServletResponse resp) throws IOException, NumberFormatException {
        int orderID;
        try {
            orderID = Integer.parseInt(req.getParameter("id"));
            if (orderID < 0) {
                resp.sendError(400, "Invalid ID");
            }
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid ID");
            throw new NumberFormatException();
        }
        return orderID;
    }
}
