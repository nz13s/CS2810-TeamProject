package sql;

import entities.Order;
import entities.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that stores SQL queries related to the restaurant_table in the database.
 *
 * @author Jatin Khatra
 * @author Bhavik Narang
 */

public class Tables {

    private PreparedStatement fetchTables;

    /**
     * Constructor that holds the SQL queries that are going to be used.
     *
     * @param connection connection to the database.
     * @throws SQLException thrown if sql logic is wrong.
     */

    public Tables(Connection connection) throws SQLException {

        fetchTables = connection.prepareStatement(
                "SELECT restaurant_table.table_num, seats_available, occupied, orders.order_id, time_ordered, order_confirmed, order_preparing, order_ready, order_served, food.food_name, food.price, food_orders.quantity " +
                        "FROM restaurant_table join orders on restaurant_table.table_num = orders.table_num, food_orders join food on food_orders.food_id = food.food_id " +
                        "WHERE orders.table_num = restaurant_table.table_num AND food_orders.order_id = orders.order_id " +
                        "ORDER BY table_num");
    }

    /**
     * Method that gets all tables that have order(s).
     *
     * @return ArrayList of Tables.
     * @throws SQLException thrown if sql logic is incorrect.
     */

    public ArrayList<Table> fetchTables() throws SQLException {
        ArrayList<Table> list = new ArrayList<Table>();
        ResultSet resultSet = fetchTables.executeQuery();
        Set<Table> tables = new HashSet<>();

        while (resultSet.next()) {
            Order order = new Order(
                    resultSet.getInt("order_id"),
                    resultSet.getLong("time_ordered"),
                    resultSet.getLong("order_confirmed"),
                    resultSet.getLong("order_preparing"),
                    resultSet.getLong("order_ready"),
                    resultSet.getLong("order_served"),
                    resultSet.getInt("table_num"),
                    null);
            Table c = tables.stream().filter(cat -> cat.getTableNum() == order.getTableNum()).findFirst()
                    .orElse(new Table(resultSet.getInt("table_num"), resultSet.getInt("seats_available"), resultSet.getBoolean("occupied"),null));
            c.addOrder(order);
            tables.add(c);
        }
        tables.forEach(list::add);
        return list;
    }

}