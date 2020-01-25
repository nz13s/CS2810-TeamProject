package databaseInit;

import sql.FoodIngredientsSql;
import sql.FoodOrdersSql;
import sql.Foods;
import sql.Orders;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

  public static Foods FOODS;
  public static FoodIngredientsSql FOOD_INGREDIENTS;
  public static FoodOrdersSql FOOD_ORDERS;
  public static Orders ORDERS;

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
    FOOD_INGREDIENTS = new FoodIngredientsSql(c);
    FOOD_ORDERS = new FoodOrdersSql(c);
    ORDERS = new Orders(c);
  }


}
