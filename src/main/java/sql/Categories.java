package sql;

import entities.Category;
import entities.Food;
import entities.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Categories {

    private PreparedStatement catById;
    private PreparedStatement foodByCatId;
    private PreparedStatement getAllCatId;

    public Categories(Connection connection) throws SQLException {
        catById = connection.prepareStatement(
                "SELECT category_id, category "
                + "FROM category "
                + "WHERE category_id = ?");

        foodByCatId = connection.prepareStatement(
                "SELECT food_id, food_name, food_description, calories, price, available, category_id "
                        + "FROM food "
                        + "WHERE category_id = ?");

        getAllCatId = connection.prepareStatement(
                "SELECT category_id "
                        + " FROM category");
    }

    // makes new Category object based on the category id.
@Nullable
    public entities.Category getCatByID(int categoryID) throws SQLException {
        catById.setInt(1, categoryID);
        ResultSet resultSet = catById.executeQuery();
        if (resultSet.next()) {
            return new entities.Category(resultSet.getInt("category_id"), resultSet.getString("category"));
        }
        return null;
    }

    public ArrayList<Food> getFoodByCatId(int categoryID) throws SQLException {
        ArrayList<Food> list = new ArrayList<Food>();

        foodByCatId.setInt(1, categoryID);
        ResultSet resultSet = foodByCatId.executeQuery();
        while (resultSet.next()) {
            list.add(new Food (
                    resultSet.getInt("food_id"),
                    resultSet.getString("food_name"),
                    resultSet.getString("food_description"),
                    resultSet.getInt("calories"),
                    resultSet.getLong("price"),
                    resultSet.getBoolean("available"),
                    resultSet.getInt("category_id")));
        }
        return list;
    }

    public Menu getMenu() throws SQLException {
        ArrayList<Integer> nums = new ArrayList<Integer>();
        ResultSet resultSet = getAllCatId.executeQuery();
        while (resultSet.next()) {
            nums.add(resultSet.getInt("category_id"));
        }

        Menu menu = new Menu();

        for (int i=0; i<nums.size(); i++) {
            Category temp = getCatByID(nums.get(i));

            ArrayList<Food> list = getFoodByCatId(nums.get(i));

            for (int j=0; j<list.size(); j++) {
                temp.addFood(list.get(j));
            }

            menu.addCat(temp);
        }

        return menu;
    }

}
