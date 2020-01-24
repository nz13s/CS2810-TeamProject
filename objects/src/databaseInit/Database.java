package databaseInit;

import sql.Foods;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

  public static Foods FOODS;

  public void connection() throws SQLException {
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

    assert c != null;
    FOODS = new Foods(c);
  }


}
