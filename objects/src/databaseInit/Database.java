package databaseInit;

import entities.Food;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

  public static Food food;

  public Connection connection() {
    Connection c = null;
    String url = "jdbc:postgresql://localhost/tomcat/?tcpKeepAlive=true";
    String username = "tomcat";
    String password = "Us17eEXAIkBs2fhZRtQr";
    try {
      c = DriverManager.getConnection(url, username, password);
      System.out.println("Connection established");
    } catch (SQLException e) {
      System.out.println("Connection failed, see below: ");
      e.printStackTrace();
    }
    return c;
  }
}
