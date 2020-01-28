// NOT FINAL, JUST FOR TESTING SQL

package endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import databaseInit.Database;
import entities.Order;
import entities.Queue;
import entities.serialisers.OrderSerialiser;
import sql.Orders;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class that converts a Queue of Orders from a database, to JSON and GETs it
 * to the frontend.
 *
 * @author Jatin
 * @author Cameron
 * @author Bhavik
 */

@WebServlet("/orders")
public class OrdersToFrontend extends HttpServlet {

    private ObjectMapper mapper;

    /**
     * Constructor that initialises the mapper attribute.
     */

    public OrdersToFrontend() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule("Serialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Queue.class, new OrderSerialiser());
        mapper.registerModule(module);
    }

    /**
     * Method that converts the objects in Queue into JSON.
     *
     * @return String that represents the Queue in JSON.
     * @throws IOException
     * @throws SQLException
     */

    public String queueToJSON() throws SQLException, IOException {
        Orders order = Database.ORDERS;

        ArrayList<Order> p = order.getOrders(0l);
        Queue q = new Queue(p);

        return mapper.writeValueAsString(q);
    }

    /**
     * Method that GETs the JSON objects.
     *
     * @param req  server request.
     * @param resp server response.
     * @throws ServletException
     * @throws IOException
     */

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        OrdersToFrontend o = this;
        try {
            pw.println(o.queueToJSON());
        } catch (SQLException e) {
            e.printStackTrace();
            pw.println(e.getMessage());
        }
        pw.flush();
    }

}
