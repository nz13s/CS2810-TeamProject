package databaseInit;

import sql.FoodIngredientsSql;
import sql.FoodOrdersSql;
import sql.Foods;
import sql.Orders;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@WebListener
public class Database implements ServletContextListener {

  private static Database instance;
  private static Connection connection;
  private static SQLException sqlException;

  public static Foods FOODS;
  public static FoodIngredientsSql FOOD_INGREDIENTS;
  public static FoodOrdersSql FOOD_ORDERS;
  public static Orders ORDERS;

  public Database(){
    instance = instance == null ? this : null; //preserve the current instance if it is not null
  }

  private void connect() throws SQLException {
    if (instance != this) return; //prevent this from running multiple times
    String url = "jdbc:postgresql://localhost/tomcat/?tcpKeepAlive=true";
    String username = "tomcat";
    String password = "Us17eEXAIkBs2fhZRtQr";
    try {
      connection = DriverManager.getConnection(url, username, password);
      System.out.println("Connection established");
    } catch (SQLException e) {
      sqlException = e;
      System.out.println("Connection failed, see below: ");
      e.printStackTrace();
    }

    if (connection == null){
      System.out.println("Error.");
      return;
    }

    FOODS = new Foods(connection);
    FOOD_INGREDIENTS = new FoodIngredientsSql(connection);
    FOOD_ORDERS = new FoodOrdersSql(connection);
    ORDERS = new Orders(connection);
  }

  public static SQLException getException(){
    return sqlException;
  }

  private void disconnect() throws SQLException{
    connection.close();
  }

  @Override
  public void contextInitialized(ServletContextEvent sce) {
    try {
      connect();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void contextDestroyed(ServletContextEvent sce) {
    try {
      disconnect();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
