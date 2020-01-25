package sql;

import entities.Food;
import entities.Order;

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
        "SELECT orderid, timeordered, orderconfirmed, orderready, orderserved, tablenum "
            + "FROM orders "
            + "WHERE orderid = ?");
    orderSave = connection.prepareStatement(
        "INSERT INTO orders (orderid, tablenum, timeordered, orderconfirmed, orderready, orderserved) "
            + "VALUES (?, ?, ?, ?, ?, ?)");
    foodSave = connection.prepareStatement(
        "INSERT INTO foodorders (orderid, foodid) VALUES (?, ?)");
  }

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

  public void saveOrder(Order o) throws SQLException {
    ArrayList<Food> foodList = o.getFoodItems();
    orderSave.setInt(1, o.getTableNum());
    orderSave.setLong(2, o.getTimeOrdered());
    orderSave.setLong(3, o.getOrderConfirmed());
    orderSave.setBoolean(4, o.orderReady());
    orderSave.setLong(5, o.getOrderServed());
    orderSave.execute();
    orderSave.close();

    for (Food food : foodList) {
      foodSave.setInt(1, o.getOrderID());
      foodSave.setInt(2, food.getFoodID());
      foodSave.addBatch();
    }
    foodSave.executeBatch();
    foodSave.close();
  }
}
