package databaseInit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Queries {

  private Connection c;

  public Queries(Connection c) {
    this.c = c;
  }

  public void getTableNo(int orderID) throws SQLException {
    String query = "SELECT table_num FROM orders WHERE order_id = " + orderID;

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d\n", resultSet.getInt("tablenum"));
    }
  }

  public void checkOccupied(int tableNum) throws SQLException {
    String query = "SELECT occupied FROM restaurant_table WHERE table_num = " + tableNum;

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%b\n", resultSet.getBoolean("occupied"));
    }
  }

  public void setOccupied(int tableNum) throws SQLException {
    String query = "UPDATE restaurant_table SET occupied WHERE table_num = " + tableNum;

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%b\n", resultSet.getBoolean("occupied"));
    }
  }

  public void checkOrderConfirmed() throws SQLException {
    String query = "SELECT order_id, table_num, time_ordered "
        + "FROM orders WHERE time_ordered != 0";

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d, %d, %d\n", resultSet.getLong("orderid"),
          resultSet.getLong("tablenum"),
          resultSet.getLong("timeordered"));
    }
  }

  public void checkOrderReady() throws SQLException {
    String query = "SELECT order_id, table_num, time_ordered "
        + "FROM orders WHERE order_ready != 0";

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d, %d, %d\n", resultSet.getLong("orderid"),
          resultSet.getLong("tablenum"),
          resultSet.getLong("timeordered"));
    }
  }

  public void checkOrderServed() throws SQLException {
    String query = "SELECT order_id, table_num, time_ordered "
        + "FROM orders WHERE order_served != 0";

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d, %d, %d\n", resultSet.getLong("orderid"),
          resultSet.getLong("tablenum"),
          resultSet.getLong("timeordered"));
    }
  }

  public void checkSeats(int guestsNo) throws SQLException {
    String query = "SELECT table_num, seats_available "
        + "FROM restaurant_table WHERE occupied = 'n'" +
        "AND seats_available >= " + guestsNo;

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d, %d\n", resultSet.getLong("tablenum"),
          resultSet.getLong("seatsavailable"));
    }
  }

}
