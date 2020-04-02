package endpoints.restricted;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import databaseInit.Database;
import entities.Order;
import entities.serialisers.CancelledOrdersSerialiser;
import entities.serialisers.OrderSerialiser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/*
* Endpoint for the frontend to call to cancel orders {@link Order}
 * Spec:
 *  GET - No params
* */
public class CancelOrder extends HttpServlet {
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
                new SimpleModule("CancelledOrdersSerialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Order.class, new CancelledOrdersSerialiser());
        om.registerModule(module);
    }

    /**
     * GETs all cancelled orders.
     *
     * @param req servlet request.
     * @param resp servlet response.
     * @throws ServletException
     * @throws IOException if input/output error occurs.
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Order> list = null;
        try {
            list = Database.ORDERS.getCancelledOrders();
        } catch (SQLException e) {
            resp.sendError(500, "Unable to get orders.");
            return;
        }
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        pw.println(om.writeValueAsString(list));
        pw.flush();
    }

    /**
     * With a given orderID, the cancelled order checkbox is updated inside the database.
     *
     * @param req servlet request.
     * @param resp servlet response.
     * @throws IOException if input/output error occurs.
     */

    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int orderID = -1;
        try {
            orderID = Integer.parseInt(req.getParameter("orderID"));
            if (orderID < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid orderID.");
            return;
        }

        try {
            Database.ORDERS.cancelOrder(orderID);
        } catch (SQLException e) {
            resp.sendError(500, "SQL Error: Failed to cancel the order.");
            return;
        }
    }
}

