package sql;

import entities.Food;

import javax.annotation.CheckForNull;
import javax.annotation.CheckReturnValue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Foods {
    private PreparedStatement foodById;
    private PreparedStatement foodByCatId;

    public Foods(Connection connection) throws SQLException {
        foodById = connection.prepareStatement(
                "SELECT food_id, food_name, food_description, calories, price, available, category_id "
                        + "FROM food "
                        + "WHERE food_id = ?");

        foodByCatId = connection.prepareStatement(
                "SELECT food_id, food_name, food_description, calories, price, available, category_id "
                        + "FROM food "
                        + "WHERE category_id = ?");
    }

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
                    resultSet.getLong("price"),
                    resultSet.getBoolean("available"),
                    resultSet.getInt("category_id"));
        }
        return null;
    }
}
