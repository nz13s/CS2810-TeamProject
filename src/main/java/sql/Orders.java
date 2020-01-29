package sql;

import entities.Item;
import entities.Order;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Orders Class deals with all order related queries
 *
 * @author Tony Delchev
 * @author Bhavik Narang
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
                "SELECT order_id, time_ordered, order_confirmed, order_preparing, order_ready, order_served, table_num "
                        + "FROM orders "
                        + "WHERE order_id = ?");
        orderSave = connection.prepareStatement(
                "INSERT INTO orders (table_num, time_ordered, order_confirmed, order_preparing, order_ready, order_served) "
                        + "VALUES (?, ?, ?, ?, ?)");
        foodSave = connection.prepareStatement(
                "INSERT INTO food_orders (order_id, food_id) VALUES (?, ?)");
        ordersGet = connection.prepareStatement(
                "SELECT o.*, i.food_id, i.order_id, f.food_name, i.quantity FROM orders AS o " +
                        "JOIN food_orders AS i ON o.order_id = i.order_id " +
                        "JOIN food f on i.food_id = f.food_id " +
                        "WHERE o.order_confirmed > ? AND o.order_served = 0");
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
                    resultSet.getLong("order_preparing"),
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
        orderSave.setLong(4, o.getOrderPreparing());
        orderSave.setBoolean(5, o.orderReady());
        orderSave.setLong(6, o.getOrderServed());
        orderSave.execute();
        ResultSet set = orderSave.getGeneratedKeys();
        if (!set.first()) {
            return false;
        }
        int order_id = set.getInt("food_id");
        orderSave.close();

        for (Item foodItem : foodList) {
            foodSave.setInt(1, order_id);
            foodSave.setInt(2, foodItem.getFoodID());
            foodSave.addBatch();
        }
        foodSave.executeBatch();
        foodSave.close();
        return true;
    }

    /**
     * Selects the completed/uncompleted orders from the database
     *
     * @param order_ready order status
     * @return Array of Orders
     * @throws SQLException if an error occurred
     */
    public ArrayList<Order> getOrders(Long order_ready) throws SQLException {
        ArrayList<Order> queue = new ArrayList<>();
        ArrayList<Order> newQueue = new ArrayList<>();
        ordersGet.setLong(1, order_ready);
        ResultSet resultSet = ordersGet.executeQuery();

        while (resultSet.next()) {
            ArrayList<Item> l = new ArrayList<>();
            l.add(new Item(resultSet.getInt("food_id"), resultSet.getString("food_name"), resultSet.getInt("quantity")));
            queue.add(new Order(
                    resultSet.getInt("order_id"),
                    resultSet.getLong("time_ordered"),
                    resultSet.getLong("order_confirmed"),
                    resultSet.getLong("order_preparing"),
                    resultSet.getLong("order_ready"),
                    resultSet.getLong("order_served"),
                    resultSet.getInt("table_num"),
                    l));
        }
        for (int i = 0; i < queue.size(); i++) {
            Order a = queue.get(i);
            for (int j = 0; j < queue.size(); j++) {
                Order b = queue.get(j);
                if (a.getOrderID() == b.getOrderID() && a != b) {
                    a.getFoodItems().add(b.getFoodItems().get(0));
                    queue.remove(b);
                }
            }
        }
        return queue;

    }


}

