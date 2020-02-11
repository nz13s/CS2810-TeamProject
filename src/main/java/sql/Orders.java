package sql;

import entities.Food;
import entities.IndexedOrder;
import entities.Item;
import entities.Order;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;

/**
 * Orders Class deals with all order related queries and methods.
 *
 * @author Tony Delchev
 * @author Bhavik Narang
 * @author Jatin Khatra
 */
public class Orders {
    private PreparedStatement orderById;
    private PreparedStatement orderSave;
    private PreparedStatement ordersGet;
    private PreparedStatement foodSave;
    private PreparedStatement orderUpdateState;
    private PreparedStatement updateOrderConfirmedByID;

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
                        + "VALUES (?, ?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
        foodSave = connection.prepareStatement(
                "INSERT INTO food_orders (order_id, food_id, quantity) VALUES (?, ?, ?)");
        ordersGet = connection.prepareStatement(
                "SELECT o.*, i.food_id, i.order_id, f.food_name, f.food_description, f.calories, f.category_id,  i.quantity FROM orders AS o " +
                        "JOIN food_orders AS i ON o.order_id = i.order_id " +
                        "JOIN food f on i.food_id = f.food_id " +
                        "WHERE o.order_confirmed != ? AND o.order_served = ?");
        orderUpdateState = connection.prepareStatement("UPDATE orders " +
                "SET order_preparing = ?, order_ready = ?, order_served = ? " +
                "WHERE order_id = ?", Statement.RETURN_GENERATED_KEYS);

        updateOrderConfirmedByID = connection.prepareStatement(
                "UPDATE orders SET order_confirmed = ? WHERE order_id = ?");
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
        orderSave.setLong(5, o.getOrderReady());
        orderSave.setLong(6, o.getOrderServed());
        orderSave.execute();
        ResultSet set = orderSave.getGeneratedKeys();
        if (!set.next()) {
            return false;
        }
        int order_id = set.getInt("order_id");

        for (Item foodItem : foodList) {
            foodSave.setInt(1, order_id);
            foodSave.setInt(2, foodItem.getFood().getFoodID());
            foodSave.setInt(3, foodItem.getAmount());
            foodSave.addBatch();
        }
        foodSave.executeBatch();
        return true;
    }

    /**
     * Selects confirmed orders from database, populates each order
     * with Array of Food items.
     *
     * @param order_ready  order status
     * @param order_served order status
     * @return Array of Orders
     * @throws SQLException if an error occurred
     */
    public ArrayList<Order> getOrders(Long order_ready, Long order_served) throws SQLException {
        ArrayList<Order> queue = new ArrayList<>();
        ArrayList<Order> newQueue = new ArrayList<>();
        ordersGet.setLong(1, order_ready);
        ordersGet.setLong(2, order_served);

        ResultSet resultSet = ordersGet.executeQuery();

        while (resultSet.next()) {
            ArrayList<Item> l = new ArrayList<>();
            //o.*, i.food_id, i.order_id, f.food_name, f.food_description, f.calories, f.category_id,  i.quantity
            Food food = new Food(resultSet.getInt("food_id"), resultSet.getString("food_name"),
                    resultSet.getString("food_description"), resultSet.getInt("calories"),
                    null, true, resultSet.getInt("category_id"),null);
            l.add(new Item(food, resultSet.getInt("quantity")));
            queue.add(new IndexedOrder(
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

    /**
     * Updates Order State by time stamping each stage the order reaches.
     * It updates every field to be able to reverse order state timestamp.
     *
     * @param order_preparing timestamp
     * @param order_ready     timestamp
     * @param order_served    timestamp
     * @param o               The order to be updated
     * @return true if successful
     * @throws SQLException if an error occurred
     */

    public boolean updateOrderState(long order_preparing, long order_ready, long order_served, Order o) throws SQLException {
        orderUpdateState.setLong(1, order_preparing);
        orderUpdateState.setLong(2, order_ready);
        orderUpdateState.setLong(3, order_served);
        orderUpdateState.setInt(4, o.getOrderID());
        orderUpdateState.execute();
        ResultSet resultSet = orderUpdateState.getGeneratedKeys();
        return resultSet.next();
    }

    /**
     * Updates "order_confirmed" to hold the current system time of when it is executed.
     *
     * @param orderID ID of the customer's order that needs to be updated.
     * @throws SQLException if sql logic is incorrect.
     */

    public void updateOrderConfirmedByID(int orderID) throws SQLException {
        updateOrderConfirmedByID.setLong(1, System.currentTimeMillis());
        updateOrderConfirmedByID.setInt(2, orderID);
        updateOrderConfirmedByID.executeUpdate();
    }


}

