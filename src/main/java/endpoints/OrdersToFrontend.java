// NOT FINAL, JUST FOR TESTING SQL

package endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import databaseInit.Database;
import entities.IndexedOrder;
import entities.Order;
import entities.Queue;
import entities.serialisers.QueueSerialiser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class that converts a Queue of Orders from a database, to JSON and GETs it
 * to the frontend.
 *
 * @author Jatin
 * @author Cameron
 * @author Bhavik
 */

public class OrdersToFrontend extends HttpServlet {

    private static ObjectMapper mapper;

    /**
     * Constructor that initialises the mapper attribute.
     */

    public OrdersToFrontend() {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule("Serialiser", new Version(1, 0, 0, null, null, null));
        module.addSerializer(Queue.class, new QueueSerialiser());
        mapper.registerModule(module);
    }

    /**
     * Method that converts the objects in Queue into JSON.
     *
     * @return String that represents the Queue in JSON.
     * @throws IOException  exception
     * @throws SQLException exception
     */
    public static String queueToJSON() throws SQLException, IOException {

        Queue q = new Queue(getOrderQueue());

        return mapper.writeValueAsString(q);
    }

    public static ArrayList<Order> getOrderQueue() throws SQLException {
        ArrayList<Order> p = Database.ORDERS.getOrders(0L, 0L);

        //add some custom payloads to serve
        List<IndexedOrder> orders = p.stream()
                .map(o -> (IndexedOrder) o)
                .sorted(
                        (o1, o2) -> (int) Math.signum(o1.findLatestTime() - o2.findLatestTime())
                )
                .collect(Collectors.toList());
        int rank = 0;
        for (Order order : orders) {
            ((IndexedOrder) order).setRank(rank++);
        }
        return p;
    }

    /**
     * Method that GETs the JSON objects.
     *
     * @param req  server request.
     * @param resp server response.
     * @throws IOException exception
     */

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        try {
            pw.println(queueToJSON());
        } catch (SQLException e) {
            e.printStackTrace();
            pw.println(e.getMessage());
        }
        pw.flush();
    }

}
