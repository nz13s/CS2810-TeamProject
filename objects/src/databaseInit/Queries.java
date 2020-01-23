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

  public void getFoodDetails(int orderID) throws SQLException {
    String query =
        "SELECT foodid FROM foodorders WHERE orderid = " + orderID + ", "
        + "SELECT * FROM food WHERE foodid = 'variable', "
        + "SELECT ingredients FROM foodingredients WHERE foodid = 'variable'";
    //I am not sure this is correct?

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d\n", resultSet.getInt("tablenum"));
    }
  }

  public void getTableNo(int orderID) throws SQLException {
    String query = "SELECT tablenum FROM orders WHERE orderid = " + orderID;

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d\n", resultSet.getInt("tablenum"));
    }
  }

  public void checkOccupied(int tableNum) throws SQLException {
    String query = "SELECT occupied FROM table WHERE tablenum = " + tableNum;

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%b\n", resultSet.getBoolean("occupied"));
    }
  }

  public void setOccupied(int tableNum) throws SQLException {
    String query = "UPDATE table SET occupied WHERE tablenum = " + tableNum;

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%b\n", resultSet.getBoolean("occupied"));
    }
  }

  public void checkOrderConfirmed() throws SQLException {
    String query = "SELECT orderid, tablenum, timeordered "
        + "FROM orders WHERE timeordered != 0";

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d, %d, %d\n", resultSet.getLong("orderid"),
          resultSet.getLong("tablenum"),
          resultSet.getLong("timeordered"));
    }
  }

  public void checkOrderReady() throws SQLException {
    String query = "SELECT orderid, tablenum, timeordered "
        + "FROM orders WHERE orderready != 0";

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d, %d, %d\n", resultSet.getLong("orderid"),
          resultSet.getLong("tablenum"),
          resultSet.getLong("timeordered"));
    }
  }

  public void checkOrderServed() throws SQLException {
    String query = "SELECT orderid, tablenum, timeordered "
        + "FROM orders WHERE orderserved != 0";

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d, %d, %d\n", resultSet.getLong("orderid"),
          resultSet.getLong("tablenum"),
          resultSet.getLong("timeordered"));
    }
  }

  public void checkSeats(int guestsNo) throws SQLException {
    String query = "SELECT tablenum, seatsavailable "
        + "FROM table WHERE occupied = 'n'" +
        "AND seatsavailable >= " + guestsNo;

    PreparedStatement preparedStatement = c.prepareStatement(query);
    ResultSet resultSet = preparedStatement.executeQuery();
    while (resultSet.next()) {
      System.out.printf("%d, %d\n", resultSet.getLong("tablenum"),
          resultSet.getLong("seatsavailable"));
    }
  }

}
