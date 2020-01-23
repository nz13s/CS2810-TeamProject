package sql;

import databaseInit.Database;
import entities.Food;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Foods {
  private PreparedStatement foodById;

  public Foods(Database db) throws SQLException {
    foodById = db.connection().prepareStatement(
        "SELECT foodid, foodname, fooddescription, calories, price, available "
            + "FROM food "
            + "WHERE foodid = ?");
  }

  public Food getFoodByID(int foodID) throws SQLException {
    foodById.setInt(1, foodID);
    ResultSet resultSet = foodById.executeQuery();
    if (resultSet.next()) {
      return new Food(
          resultSet.getInt("foodid"),
          resultSet.getString("foodname"),
          resultSet.getString("fooddescription"),
          resultSet.getInt("calories"),
          resultSet.getLong("price"),
          resultSet.getBoolean("available"));
    }
    return null;
  }
}
