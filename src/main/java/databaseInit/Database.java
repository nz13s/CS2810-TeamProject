package databaseInit;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import sql.*;

/**
 * The class to initialise the Database.
 *
 * @author Jatin Khatra, Oliver Graham
 */
@WebListener
public class Database implements ServletContextListener {

  private static Database instance;
  private static Connection connection;
  private static SQLException sqlException;

  public static Foods FOODS;
  public static FoodIngredientsSql FOOD_INGREDIENTS;
  public static FoodOrdersSql FOOD_ORDERS;
  public static Orders ORDERS;
  public static Authentication AUTHENTICATION;
  public static Categories CATEGORIES;
  public static Tables TABLES;

  public Database() {
    instance = instance == null ? this : null; //preserve the current instance if it is not null
  }

  /**
   * Establishes a connection to the database using preset url, username and pwd.
   *
   * @throws SQLException if connection fails.
   */
  private void connect() throws SQLException {
    if (instance != this) {
      return; //prevent this from running multiple times
    }
    String url = "jdbc:postgresql://localhost/tomcat?tcpKeepAlive=true";
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

    if (connection == null) {
      System.out.println("Error.");
      return;
    }

    FOODS = new Foods(connection);
    FOOD_INGREDIENTS = new FoodIngredientsSql(connection);
    FOOD_ORDERS = new FoodOrdersSql(connection);
    ORDERS = new Orders(connection);
    AUTHENTICATION = new Authentication(connection);
    CATEGORIES = new Categories(connection);
    TABLES = new Tables(connection);
  }

  public static SQLException getException() {
    return sqlException;
  }

  private void disconnect() throws SQLException {
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
