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
        "SELECT food_id, ingredients "
        + "FROM food_ingredients "
        + "WHERE food_id = ?");
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
