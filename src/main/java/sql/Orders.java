package sql;

import entities.Food;
import entities.IndexedOrder;
import entities.Item;
import entities.Order;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

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
    private PreparedStatement updateOrderPreparingByID;
    private PreparedStatement updateOrderReadyByID;
    private PreparedStatement updateOrderServedByID;
    private PreparedStatement orderConfirm;
    private PreparedStatement ordersGetUnconfirmed;


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
        ordersGetUnconfirmed = connection.prepareStatement("SELECT * " +
                "FROM orders " +
                "WHERE order_confirmed = 0 AND table_num = ?");
        orderUpdateState = connection.prepareStatement(
                "UPDATE orders " +
                        "SET order_preparing = ?, order_ready = ?, order_served = ? " +
                        "WHERE order_id = ?", Statement.RETURN_GENERATED_KEYS);
        orderConfirm = connection.prepareStatement("UPDATE orders " +
                "SET order_confirmed = ?" +
                "WHERE order_id = ?");
        updateOrderConfirmedByID = connection.prepareStatement(
                "UPDATE orders " +
                        "SET order_confirmed = ? " +
                        "WHERE order_id = ?");
        updateOrderPreparingByID = connection.prepareStatement(
                "UPDATE orders " +
                        "SET order_preparing = ? " +
                        "WHERE order_id = ?");
        updateOrderReadyByID = connection.prepareStatement(
                "UPDATE orders " +
                        "SET order_ready = ? " +
                        "WHERE order_id = ?");
        updateOrderServedByID = connection.prepareStatement(
                "UPDATE orders " +
                        "SET order_served = ? " +
                        "WHERE order_id = ?");
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
        HashMap<Integer, Order> orders = new HashMap<>();
        ordersGet.setLong(1, order_ready);
        ordersGet.setLong(2, order_served);

        ResultSet resultSet = ordersGet.executeQuery();

        while (resultSet.next()) {
            //o.*, i.food_id, i.order_id, f.food_name, f.food_description, f.calories, f.category_id,  i.quantity
            Food food = new Food(resultSet.getInt("food_id"), resultSet.getString("food_name"),
                    resultSet.getString("food_description"), resultSet.getInt("calories"),
                    null, true, resultSet.getInt("category_id"), null, null);
            int orderID = resultSet.getInt("order_id");
            if (orders.containsKey(orderID)) {
                orders.get(orderID).addFoodItem(new Item(food, resultSet.getInt("quantity")));
            } else {
                ArrayList<Item> items = new ArrayList<>();
                items.add(new Item(food, resultSet.getInt("quantity")));
                orders.put(orderID, new IndexedOrder(
                        resultSet.getInt("order_id"),
                        resultSet.getLong("time_ordered"),
                        resultSet.getLong("order_confirmed"),
                        resultSet.getLong("order_preparing"),
                        resultSet.getLong("order_ready"),
                        resultSet.getLong("order_served"),
                        resultSet.getInt("table_num"),
                        items));
            }
        }
        return new ArrayList<>(orders.values());

    }

    public ArrayList<Order> getOrdersUnconfirmed(int tableNum) throws SQLException {
        HashMap<Integer, Order> orders = new HashMap<>();
        ordersGetUnconfirmed.setInt(1, tableNum);

        ResultSet resultSet = ordersGetUnconfirmed.executeQuery();
        while (resultSet.next()) {
            //o.*, i.food_id, i.order_id, f.food_name, f.food_description, f.calories, f.category_id,  i.quantity
            Food food = new Food(resultSet.getInt("food_id"), resultSet.getString("food_name"),
                    resultSet.getString("food_description"), resultSet.getInt("calories"),
                    null, true, resultSet.getInt("category_id"), null, null);
            int orderID = resultSet.getInt("order_id");
            if (orders.containsKey(orderID)) {
                orders.get(orderID).addFoodItem(new Item(food, resultSet.getInt("quantity")));
            } else {
                ArrayList<Item> items = new ArrayList<>();
                items.add(new Item(food, resultSet.getInt("quantity")));
                orders.put(orderID, new IndexedOrder(
                        resultSet.getInt("order_id"),
                        resultSet.getLong("time_ordered"),
                        resultSet.getLong("order_confirmed"),
                        resultSet.getLong("order_preparing"),
                        resultSet.getLong("order_ready"),
                        resultSet.getLong("order_served"),
                        resultSet.getInt("table_num"),
                        items));
            }
        }
        return new ArrayList<>(orders.values());

    }

    /**
     * Updates Order State by time stamping each stage the order reaches.
     * It updates every field to be able to reverse order state timestamp.
     *
     * @param o The order to be updated
     * @return true if successful
     * @throws SQLException if an error occurred
     */

    public boolean updateOrderState(Order o, int state) throws SQLException {
        switch (state) {
            case 0:
                orderUpdateState.setLong(1, 0);
                orderUpdateState.setLong(2, 0);
                orderUpdateState.setLong(3, 0);
                orderUpdateState.setInt(4, o.getOrderID());
                break;
            case 1:
                orderUpdateState.setLong(1, System.currentTimeMillis());
                orderUpdateState.setLong(2, 0);
                orderUpdateState.setLong(3, 0);
                orderUpdateState.setInt(4, o.getOrderID());
                break;
            case 2:
                orderUpdateState.setLong(1, o.getOrderPreparing());
                orderUpdateState.setLong(2, System.currentTimeMillis());
                orderUpdateState.setLong(3, 0);
                orderUpdateState.setInt(4, o.getOrderID());
                break;
            case 3:
                orderUpdateState.setLong(1, o.getOrderPreparing());
                orderUpdateState.setLong(2, o.getOrderReady());
                orderUpdateState.setLong(3, System.currentTimeMillis());
                orderUpdateState.setInt(4, o.getOrderID());
                break;
            default:
                return false;
        }
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

    /**
     * Updates "order_preparing" to hold the current system time of when it is executed.
     *
     * @param orderID ID of the customer's order that needs to be updated.
     * @throws SQLException if sql logic is incorrect.
     */

    public void updateOrderPreparingByID(int orderID) throws SQLException {
        updateOrderPreparingByID.setLong(1, System.currentTimeMillis());
        updateOrderPreparingByID.setInt(2, orderID);
        updateOrderPreparingByID.executeUpdate();
    }

    /**
     * Updates "order_ready" to hold the current system time of when it is executed.
     *
     * @param orderID ID of the customer's order that needs to be updated.
     * @throws SQLException if sql logic is incorrect.
     */

    public void updateOrderReadyByID(int orderID) throws SQLException {
        updateOrderReadyByID.setLong(1, System.currentTimeMillis());
        updateOrderReadyByID.setInt(2, orderID);
        updateOrderReadyByID.executeUpdate();
    }

    /**
     * Updates "order_served" to hold the current system time of when it is executed.
     *
     * @param orderID ID of the customer's order that needs to be updated.
     * @throws SQLException if sql logic is incorrect.
     */

    public void updateOrderServedByID(int orderID) throws SQLException {
        updateOrderServedByID.setLong(1, System.currentTimeMillis());
        updateOrderServedByID.setInt(2, orderID);
        updateOrderServedByID.executeUpdate();
    }

    public void confirmOrder(int orderID) throws SQLException {
        orderConfirm.setLong(1, System.currentTimeMillis());
        orderConfirm.setInt(2, orderID);
        orderConfirm.execute();
    }

}

