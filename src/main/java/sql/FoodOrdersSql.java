package sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Class that stores SQL queries related to the food_orders table.
 *
 * @author Nick Bogachev
 */
public class FoodOrdersSql {
  private PreparedStatement getOrderbyFoodId;

  /**
   * Constructor that holds the SQL queries that are going to be used.
   *
   * @param connection connection to the database.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public FoodOrdersSql(Connection connection) throws SQLException {
    getOrderbyFoodId = connection.prepareStatement(
            "SELECT food_id, order_id, quantity "
                    + "FROM food_orders "
                    + "WHERE food_id = ?"
    );
  }
}
