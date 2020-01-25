package sql;

import entities.FoodIngredients;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodIngredientsSql {
  private PreparedStatement getIngredients;

  public FoodIngredientsSql(Connection connection) throws SQLException {
    getIngredients = connection.prepareStatement(
        "SELECT foodid, ingredients "
        + "FROM foodingredients "
        + "WHERE foodid = ?");
  }

  public FoodIngredients getIngredientsById(int foodid) throws SQLException {
    getIngredients.setInt(1, foodid);
    ResultSet resultSet = getIngredients.executeQuery();
    if (resultSet.next()) {
      return new FoodIngredients(
          resultSet.getInt("foodid"),
          resultSet.getString("ingredients"));
    }
    return null;
  }
}
