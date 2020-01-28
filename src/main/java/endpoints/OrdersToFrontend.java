// NOT FINAL, JUST FOR TESTING SQL

package endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import databaseInit.Database;
import entities.Item;
import entities.Order;
import sql.Orders;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.ArrayList;

@WebServlet("/orders")
public class OrdersToFrontend extends HttpServlet {

    private ObjectMapper mapper;

    public OrdersToFrontend() {
        mapper = new ObjectMapper();
    }

    // you can change the method return type to whatever you want.
    public String testMethods() throws SQLException {

        Orders order = Database.ORDERS;

        // you can do whatever you want here.
        ArrayList<Order> list = order.getOrders((long) 3);
        ArrayList<Item> item = new ArrayList<>();
        String a = "";
        String b = "";

        for (int i = 0; i < list.size(); i++) {
            for (int j = 0; j < list.get(i).getFoodItems().size(); j++) {
                item = list.get(i).getFoodItems();
                a += item.get(j).getName() + "/ ";
                b += list.get(i).getOrderID() + " ";
            }
        }
        return a + b;
    }

    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter pw = resp.getWriter();
        OrdersToFrontend o = this;
        try {
            pw.println(o.testMethods());
        } catch (SQLException e) {
            e.printStackTrace();
            pw.println(e.getMessage());
        }
        pw.flush();
    }

}
