package sql;

import entities.FoodIngredients;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that stores SQL queries related to the food_ingredients table.
 *
 * @author Nick
 */

public class FoodIngredientsSql {
  private PreparedStatement getIngredients;
  private PreparedStatement s;

  /**
   * Constructor that holds the SQL queries that are going to be used.
   *
   * @author Jatin
   * @param connection connection to the database.
   * @throws SQLException thrown if sql logic is wrong.
   */

  public FoodIngredientsSql(Connection connection) throws SQLException {

    getIngredients = connection.prepareStatement(
            "SELECT ingredient_id, ingredient "
            + "FROM ingredients, food "
            + "WHERE (ingredients.ingredient_id = food.food_id AND food_id = ?)");
  }

  /**
   * Method that gets an ingredient based on the foodID.
   *
   * @param foodID foodID of the food.
   * @return FoodIngredients object, based on the sql query output.
   * @throws SQLException thrown if sql logic is wrong.
   */

  @CheckForNull
  @CheckReturnValue
  public FoodIngredients getIngredientsById(int foodID) throws SQLException {
    getIngredients.setInt(1, foodID);
    ResultSet resultSet = getIngredients.executeQuery();
    if (resultSet.next()) {
      return new FoodIngredients(
          resultSet.getInt("food_id"),
          resultSet.getString("ingredient"));
    }
    return null;
  }
}
