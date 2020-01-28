package sql;

import entities.Item;
import entities.Order;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Orders Class deals with all order related queries
 *
 * @author Tony Delchev
 */
public class Orders {
    private PreparedStatement orderById;
    private PreparedStatement orderSave;
    private PreparedStatement ordersGet;
    private PreparedStatement foodSave;

    /**
     * Constructor creates the prepared Statements to save time on execution
     *
     * @param connection the connection to the DB
     * @throws SQLException if an error occurred
     */
    public Orders(Connection connection) throws SQLException {
        orderById = connection.prepareStatement(
                "SELECT order_id, time_ordered, order_confirmed, order_ready, order_served, table_num "
                        + "FROM orders "
                        + "WHERE order_id = ?");
        orderSave = connection.prepareStatement(
                "INSERT INTO orders (table_num, time_ordered, order_confirmed, order_ready, order_served) "
                        + "VALUES (?, ?, ?, ?, ?)");
        foodSave = connection.prepareStatement(
                "INSERT INTO food_orders (order_id, food_id) VALUES (?, ?)");
        ordersGet = connection.prepareStatement(
                "SELECT o.*, i.food_id, i.order_id, f.name, i.quantity FROM orders AS o " +
                        "JOIN food_orders AS i ON o.order_id = i.order_id " +
                        "JOIN food f on i.food_id = f.food_id " +
                        "WHERE o.order_ready = ?");
    }

    /**
     * Selects the order record matching the orderID
     *
     * @param orderID order id number
     * @return Order object
     * @throws SQLException if an error occurred
     */
    @Nullable
    public Order getOrderByID(int orderID) throws SQLException {
        orderById.setInt(1, orderID);
        ResultSet resultSet = orderById.executeQuery();
        if (resultSet.next()) {
            return new Order(
                    resultSet.getInt("order_id"),
                    resultSet.getLong("time_ordered"),
                    resultSet.getLong("order_confirmed"),
                    resultSet.getLong("order_ready"),
                    resultSet.getLong("order_served"),
                    resultSet.getInt("table_num"),
                    null);
        }
        return null;
    }

    /**
     * Saves the order to the database
     *
     * @param o the order to save
     * @return false if failure, else true
     * @throws SQLException if an error occurred
     */
    public boolean saveOrder(@Nonnull Order o) throws SQLException {
        ArrayList<Item> foodList = o.getFoodItems();
        orderSave.setInt(1, o.getTableNum());
        orderSave.setLong(2, o.getTimeOrdered());
        orderSave.setLong(3, o.getOrderConfirmed());
        orderSave.setBoolean(4, o.orderReady());
        orderSave.setLong(5, o.getOrderServed());
        orderSave.execute();
        ResultSet set = orderSave.getGeneratedKeys();
        if (!set.first()) {
            return false;
        }
        int order_id = set.getInt("food_id");
        orderSave.close();

        for (Item foodItem : foodList) {
            foodSave.setInt(1, order_id);
            foodSave.setInt(2, foodItem.getFood().getFoodID());
            foodSave.addBatch();
        }
        foodSave.executeBatch();
        foodSave.close();
        return true;
    }

    /**
     * Selects the completed/uncompleted orders from the database
     *
     * @param completed order status
     * @return Array of Orders
     * @throws SQLException if an error occurred
     */
    public ArrayList<Order> getOrders(Boolean completed) throws SQLException {
        ArrayList<Order> queue = new ArrayList<>();
        ordersGet.setBoolean(1, completed);
        ResultSet resultSet = ordersGet.executeQuery();
        while (resultSet.next()) {
            ArrayList<Item> l = new ArrayList<>();
            l.add(new Item(resultSet.getInt("food_id"), resultSet.getInt("amount")));
            queue.add(new Order(
                    resultSet.getInt("order_id"),
                    resultSet.getLong("time_ordered"),
                    resultSet.getLong("order_confirmed"),
                    resultSet.getLong("order_ready"),
                    resultSet.getLong("order_served"),
                    resultSet.getInt("table_num"),
                    l));

        }
        return queue;
    }
}
