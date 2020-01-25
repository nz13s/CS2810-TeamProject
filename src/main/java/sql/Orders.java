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

public class Orders {
  private PreparedStatement orderById;
  private PreparedStatement orderSave;
  private PreparedStatement foodSave;


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
  }

  @Nullable
  public Order getOrderByID(int orderID) throws SQLException {
    orderById.setInt(1, orderID);
    ResultSet resultSet = orderById.executeQuery();
    if (resultSet.next()) {
      return new Order(
          resultSet.getInt("orderid"),
          resultSet.getLong("timeordered"),
          resultSet.getLong("orderconfirmed"),
          resultSet.getLong("orderready"),
          resultSet.getLong("orderserved"),
          resultSet.getInt("tablenum"),
          null);
    }
    return null;
  }

  /**
   * Saves the order to the database
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
    if (!set.first()){
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
}
