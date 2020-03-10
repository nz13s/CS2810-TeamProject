package sql;

import entities.FoodIngredients;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores SQL queries related to the food_ingredients table.
 *
 * @author Nick
 */

public class FoodIngredientsSql {
    private PreparedStatement getAllIngredients;
    private PreparedStatement addNewIngredients;

    /**
     * Constructor that holds the SQL queries that are going to be used.
     *
     * @param connection connection to the database.
     * @throws SQLException thrown if sql logic is wrong.
     * @author Jatin
     * @author Bhavik
     */

    public FoodIngredientsSql(Connection connection) throws SQLException {

        getAllIngredients = connection.prepareStatement(
                "SELECT ingredient_id, ingredient, allergen FROM ingredients;"
        );
        addNewIngredients = connection.prepareStatement(
                "INSERT INTO ingredients(ingredient, allergen) values(ingredient, allergen);", PreparedStatement.RETURN_GENERATED_KEYS
        );
    }

    /**
     * Gets the current set of ingredients
     *
     * @return the non-null, but possibly empty list of ingredients
     * @throws SQLException on a (fatal) error in communicating with DB
     */
    @CheckReturnValue
    @Nonnull
    public List<FoodIngredients> getAllIngredients() throws SQLException {
        List<FoodIngredients> ingredients = new ArrayList<>();
        ResultSet resultSet = getAllIngredients.executeQuery();
        while (resultSet.next()) {
            ingredients.add(new FoodIngredients(
                    resultSet.getInt("food_id"),
                    resultSet.getString("ingredient"),
                    resultSet.getBoolean("allergen")
            ));
        }
        return ingredients;
    }

    /**
     * Adds a new ingredient
     *
     * @param name       name of new ingredient
     * @param isAllergen true if allergen, else false
     * @return the generated ID of the ingredient
     * @throws SQLException on a (fatal) error in communicating with DB
     */
    public int addIngredient(String name, boolean isAllergen) throws SQLException {
        addNewIngredients.setString(1, name);
        addNewIngredients.setBoolean(2, isAllergen);
        addNewIngredients.executeUpdate();

        ResultSet resultSet = addNewIngredients.getGeneratedKeys();
        resultSet.next();
        return resultSet.getInt("ingredient_id");
    }
}
