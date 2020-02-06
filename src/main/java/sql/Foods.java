package sql;

import entities.Food;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Class that stores SQL queries related to the food table.
 *
 * @author Nick
 */

public class Foods {

    private PreparedStatement foodById;
    private PreparedStatement updateAvailability;

    /**
     * Constructor that holds the SQL queries that are going to be used.
     *
     * @param connection connection to the database.
     * @throws SQLException thrown if sql logic is wrong.
     */

    public Foods(Connection connection) throws SQLException {
        foodById = connection.prepareStatement(
                "SELECT food_id, food_name, food_description, calories, price, available, category_id "
                        + "FROM food "
                        + "WHERE food_id = ?");

        updateAvailability = connection.prepareStatement(
                "UPDATE public.food SET available = ? WHERE food_id = ?");
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
    public Food getFoodByID(int foodID) throws SQLException {
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
                    resultSet.getInt("category_id"));
        }
        return null;
    }

    /**
     * Method that updates the availability of a food based on the ID.
     *
     * @param foodID foodID of the food.
     * @param availability availability of the food.
     * @throws SQLException thrown if sql logic is wrong.
     */

    public void updateAvailability(int foodID, boolean availability) throws SQLException {
        updateAvailability.setBoolean(1, availability);
        updateAvailability.setInt(2, foodID);
        updateAvailability.executeUpdate();
    }
}
