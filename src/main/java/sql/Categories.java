package sql;

import entities.Category;
import entities.Food;
import entities.Menu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Class that stores SQL queries related to the categories table.
 *
 * @author Jatin
 */

public class Categories {

    private PreparedStatement catById;
    private PreparedStatement foodByCatId;
    private PreparedStatement getAllCatId;

    /**
     * Constructor that holds the SQL queries that are going to be used.
     *
     * @param connection connection to the database.
     * @throws SQLException thrown if sql logic is wrong.
     */

    public Categories(Connection connection) throws SQLException {
        catById = connection.prepareStatement(
                "SELECT category_id, category "
                        + "FROM categories "
                        + "WHERE category_id = ?");

        foodByCatId = connection.prepareStatement(
                "SELECT food_id, food_name, food_description, calories, price, available, category_id "
                        + "FROM food "
                        + "WHERE category_id = ?");

        getAllCatId = connection.prepareStatement(
                "SELECT category_id "
                        + " FROM categories");
    }

    /**
     * Method that gets a category based on the ID.
     *
     * @param categoryID categoryID of the category.
     * @return Category object, based on the sql query output.
     * @throws SQLException thrown if sql logic is wrong.
     */

    @Nullable
    public entities.Category getCatByID(int categoryID) throws SQLException {
        catById.setInt(1, categoryID);
        ResultSet resultSet = catById.executeQuery();
        if (resultSet.next()) {
            //return new entities.Category(resultSet.getInt("category_id"), resultSet.getString("category"));
            return new Category(1,"error");
        }
        return null;
    }

    /**
     * Method that gets all foods based on a given categoryID and stores it into an ArrayList.
     *
     * @param categoryID categoryID of the category.
     * @return ArrayList that contains Food objects based on categoryID.
     * @throws SQLException thrown if sql logic is wrong.
     */

    @Nonnull
    public ArrayList<Food> getFoodByCatId(int categoryID) throws SQLException {
        ArrayList<Food> list = new ArrayList<Food>();

        foodByCatId.setInt(1, categoryID);
        ResultSet resultSet = foodByCatId.executeQuery();
        while (resultSet.next()) {
            list.add(new Food(
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

    /**
     * Method that organises all categories and foods into an ArrayList to represent
     * the restaurant's menu, and stores all information into a Menu object.
     *
     * @return Menu object that holds all categories and foods, based on the database.
     * @throws SQLException thrown if sql logic is wrong.
     */

    public Menu getMenu() throws SQLException {
        ArrayList<Integer> nums = new ArrayList<Integer>();
        ResultSet resultSet = getAllCatId.executeQuery();

        // adds all category numbers into an ArrayList.
        while (resultSet.next()) {
            nums.add(resultSet.getInt("category_id"));
        }

        Menu menu = new Menu();

        // loop through all categoryIDs.
        for (int i = 0; i < nums.size(); i++) {
            // make a temp Category object.
            // Foods that relate to this Category will be added.
            Category temp = getCatByID(nums.get(i));

            // ArrayList that holds all Food objects for the temp Category.
            ArrayList<Food> list = getFoodByCatId(nums.get(i));

            // loop that adds each Food object to the temp category.
            for (int j = 0; j < list.size(); j++) {
                temp.addFood(list.get(j));
            }

            // temp category is added to a Menu object.
            menu.addCat(temp);
        }

        return menu;
    }

}
