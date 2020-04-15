package sql;

import databaseInit.Database;
import entities.Food;
import entities.Ingredient;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;


/**
 * Class that stores SQL queries related to the food table.
 *
 * @author Nick Bogachev, Jatin Khatra, Oliver Graham, Bhavik Narang, Cameron Jones
 */
public class Foods {
  private PreparedStatement foodById;
  private PreparedStatement updateFoodNameById;
  private PreparedStatement updateFoodDescriptionById;
  private PreparedStatement updateCaloriesById;
  private PreparedStatement updatePriceById;
  private PreparedStatement updateAvailabilityById;
  private PreparedStatement updateCategoryIdById;
  private PreparedStatement newFood;
  private PreparedStatement linkFoodIngredient;

  /**
   * Constructor that holds the SQL queries that are going to be used.
   *
   * @param connection connection to the database.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public Foods(Connection connection) throws SQLException {
    foodById = connection.prepareStatement(
            "SELECT food_id, food_name, food_description, calories, price, available, "
                    + "category_id, image_url "
                    + "FROM food "
                    + "WHERE food_id = ?");

    updateFoodNameById = connection.prepareStatement(
            "UPDATE food "
                    + "SET food_name = ? "
                    + "WHERE food_id = ?");

    updateFoodDescriptionById = connection.prepareStatement(
            "UPDATE food "
                    + "SET food_description = ? "
                    + "WHERE food_id = ?");

    updateCaloriesById = connection.prepareStatement(
            "UPDATE food "
                    + "SET calories = ? "
                    + "WHERE food_id = ?");

    updatePriceById = connection.prepareStatement(
            "UPDATE food "
                    + "SET price = ? "
                    + "WHERE food_id = ?");

    updateAvailabilityById = connection.prepareStatement(
            "UPDATE food "
                    + "SET available = ? "
                    + "WHERE food_id = ?");

    updateCategoryIdById = connection.prepareStatement(
            "UPDATE food "
                    + "SET category_id = ? "
                    + "WHERE food_id = ?");

    newFood = connection.prepareStatement(
            "INSERT INTO food (food_name, food_description, calories, price, available, "
                    + "category_id, image_url) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);

    linkFoodIngredient = connection.prepareStatement(
            "INSERT INTO food_ingredients (food_id, ingredient_id) "
                    + "VALUES (?, ?)");
  }

  /**
   * Method that gets a food based on the ID and populates it with ingredients if needed.
   *
   * @param foodID              foodID of the food.
   * @param populateIngredients if the ingredients needs to be populated with the foods or not.
   * @return Food object, based on the sql query output.
   * @throws SQLException thrown if sql logic is wrong.
   */
  @CheckForNull
  @CheckReturnValue
  public Food getFoodByID(int foodID, boolean populateIngredients) throws SQLException {
    if (!populateIngredients) {
      return getFoodByIdNoPopulateIngredients(foodID);
    } else {
      return getFoodByIdPopulateIngredients(foodID);
    }
  }

  /**
   * Method that gets a food based on the ID.
   *
   * @param foodID foodID of the food.
   * @return Food object, based on the sql query output.
   * @throws SQLException thrown if sql logic is wrong.
   */
  @CheckForNull
  @CheckReturnValue
  public Food getFoodByIdNoPopulateIngredients(int foodID) throws SQLException {
    foodById.setInt(1, foodID);
    ResultSet resultSet = foodById.executeQuery();
    if (resultSet.next()) {
      return new Food(
              resultSet.getInt("food_id"),
              resultSet.getString("food_name"),
              resultSet.getString("food_description"),
              resultSet.getInt("calories"),
              resultSet.getBigDecimal("price"),
              resultSet.getBoolean("available"),
              resultSet.getInt("category_id"),
              null,
              resultSet.getString("image_url"));
    }
    return null;
  }

  /**
   * Method that gets a food and its ingredients based on the ID.
   *
   * @param foodID foodID of the food.
   * @return Food object, based on the sql query output.
   * @throws SQLException thrown if sql logic is wrong.
   */
  @CheckForNull
  @CheckReturnValue
  public Food getFoodByIdPopulateIngredients(int foodID) throws SQLException {
    foodById.setInt(1, foodID);
    ResultSet resultSet = foodById.executeQuery();
    ArrayList<Ingredient> ingredients = Database.CATEGORIES.fetchIngredients();
    if (resultSet.next()) {
      Food food = new Food(
              resultSet.getInt("food_id"),
              resultSet.getString("food_name"),
              resultSet.getString("food_description"),
              resultSet.getInt("calories"),
              resultSet.getBigDecimal("price"),
              resultSet.getBoolean("available"),
              resultSet.getInt("category_id"),
              new ArrayList<Ingredient>(),
              resultSet.getString("image_url"));

      for (Ingredient ingredient : ingredients) {
        if (food.getFoodID() == ingredient.getFoodID()) {
          food.addIngredient(ingredient);
        }
      }
      return food;
    }
    return null;
  }

  /**
   * Method that updates the food_name of a food based on the ID.
   *
   * @param foodID foodID of the food.
   * @param name   name of the food.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public void updateFoodName(int foodID, String name) throws SQLException {
    updateFoodNameById.setString(1, name);
    updateFoodNameById.setInt(2, foodID);
    updateFoodNameById.executeUpdate();
  }

  /**
   * Method that updates the food_description of a food based on the ID.
   *
   * @param foodID      foodID of the food.
   * @param description description of the food.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public void updateFoodDescription(int foodID, String description) throws SQLException {
    updateFoodDescriptionById.setString(1, description);
    updateFoodDescriptionById.setInt(2, foodID);
    updateFoodDescriptionById.executeUpdate();
  }

  /**
   * Method that updates the calories of a food based on the ID.
   *
   * @param foodID   foodID of the food.
   * @param calories calories of the food.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public void updateCalories(int foodID, int calories) throws SQLException {
    updateCaloriesById.setInt(1, calories);
    updateCaloriesById.setInt(2, foodID);
    updateCaloriesById.executeUpdate();
  }

  /**
   * Method that updates the price of a food based on the ID.
   *
   * @param foodID foodID of the food.
   * @param price  price of the food.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public void updatePrice(int foodID, BigDecimal price) throws SQLException {
    updatePriceById.setBigDecimal(1, price);
    updatePriceById.setInt(2, foodID);
    updatePriceById.executeUpdate();
  }

  /**
   * Method that updates the availability of a food based on the ID.
   *
   * @param foodID       foodID of the food.
   * @param availability availability of the food.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public void updateAvailability(int foodID, boolean availability) throws SQLException {
    updateAvailabilityById.setBoolean(1, availability);
    updateAvailabilityById.setInt(2, foodID);
    updateAvailabilityById.executeUpdate();
  }

  /**
   * Method that updates the category of a food based on the ID.
   *
   * @param foodID   foodID of the food.
   * @param category category of the food.
   * @throws SQLException thrown if sql logic is wrong.
   */
  public void updateCategoryId(int foodID, int category) throws SQLException {
    updateCategoryIdById.setInt(1, category);
    updateCategoryIdById.setInt(2, foodID);
    updateCategoryIdById.executeUpdate();
  }

  /**
   * Method to add a new Food to the database.
   *
   * @param food The new Food item to add to the database.
   * @throws SQLException If a database access error occurs or this method is called
   *                      on a closed connection.
   */
  public void newFood(Food food) throws SQLException {
    newFood.setString(1, food.getFoodName());
    newFood.setString(2, food.getFoodDescription());
    newFood.setInt(3, food.getCalories());
    newFood.setBigDecimal(4, food.getPrice());
    newFood.setBoolean(5, true);
    newFood.setInt(6, food.getCategoryID());
    newFood.setString(7, food.getImageUrl());
    newFood.execute();
    ResultSet resultSet = newFood.getGeneratedKeys();
    resultSet.next();
    linkIngredients(resultSet.getInt("food_id"), food.getIngredients());
  }

  /**
   * Method to link all the {@link Ingredient}'s passed in to the {@link Food} item they are
   * passed with.
   *
   * @param foodID      The ID of the new food item as stored in the database
   * @param ingredients An Arraylist of the ingredients in the food item
   * @throws SQLException If a database access error occurs or this method is
   *                      called on a closed connection.
   */
  private void linkIngredients(int foodID, ArrayList<Ingredient> ingredients) throws SQLException {
    for (Ingredient ingredient : ingredients) {
      linkFoodIngredient.setInt(1, foodID);
      linkFoodIngredient.setInt(2, ingredient.getIngredientID());
      linkFoodIngredient.addBatch();
    }
    linkFoodIngredient.executeBatch();
  }
}