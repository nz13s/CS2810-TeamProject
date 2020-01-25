package sql;

import entities.FoodOrders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodOrdersSql {
  private PreparedStatement getOrderbyFoodId;

  public FoodOrdersSql(Connection connection) throws SQLException {
    getOrderbyFoodId = connection.prepareStatement(
        "SELECT foodid, orderid "
            + "FROM foodorders "
            + "WHERE foodid = ?"
    );
  }

  public FoodOrders getOrderByFoodId(int foodid) throws SQLException {
    getOrderbyFoodId.setInt(1, foodid);
    ResultSet resultSet = getOrderbyFoodId.executeQuery();
    if (resultSet.next()) {
      return new FoodOrders(
          resultSet.getInt("orderid"),
          resultSet.getInt("foodid"));
    }
    return null;
  }
}
