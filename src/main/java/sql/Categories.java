package sql;

import entities.Category;
import entities.Food;
import entities.Ingredient;
import entities.Menu;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Class that stores SQL queries related to the categories table.
 *
 * @author Jatin
 * @author Anas
 * @author Bhavik
 */

public class Categories {

    private PreparedStatement catById;
    private PreparedStatement foodByCatId;
    private PreparedStatement getAllCatId;
    private PreparedStatement fetchMenu;
    private PreparedStatement fetchIngredients;

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
                "SELECT food_id, food_name, food_description, calories, price, available, category_id, image_url "
                        + "FROM food "
                        + "WHERE category_id = ?");

        fetchMenu = connection.prepareStatement(
                "SELECT food_id, food_name, food_description, calories, price, available, category, c.category_id, image_url " +
                        "FROM food " +
                        "JOIN categories c on food.category_id = c.category_id " +
                        "WHERE available = TRUE " +
                        "ORDER BY c.category_id");

        fetchIngredients = connection.prepareStatement(
                "SELECT fi.food_id, fi.ingredient_id, i.ingredient, i.allergen\n" +
                        "FROM food\n" +
                        "JOIN food_ingredients fi on fi.food_id = food.food_id\n" +
                        "JOIN ingredients i on fi.ingredient_id = i.ingredient_id\n" +
                        "ORDER BY fi.food_id");
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
                    resultSet.getBigDecimal("price"),
                    resultSet.getBoolean("available"),
                    resultSet.getInt("category_id"),
                    null,
                    resultSet.getString("image_url")));
        }
        return list;
    }

    public ArrayList<Ingredient> fetchIngredients() throws SQLException {
        ArrayList<Ingredient> list = new ArrayList<Ingredient>();
        ResultSet resultSet = fetchIngredients.executeQuery();

        while (resultSet.next()) {
            list.add(new Ingredient(
                    resultSet.getInt("food_id"),
                    resultSet.getInt("ingredient_id"),
                    resultSet.getString("ingredient"),
                    resultSet.getBoolean("allergen")));
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

    @Nonnull
    public Menu fetchMenu() throws SQLException {
        Menu menu = new Menu();
        ResultSet resultSet = fetchMenu.executeQuery();
        Set<Category> categories = new HashSet<>();

        ArrayList<Ingredient> ingredients = fetchIngredients();

        while (resultSet.next()){
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
            Category c = categories.stream().filter(cat -> cat.getCategoryNumber() == food.getCategoryID()).findFirst()
                    .orElse(new Category(resultSet.getInt("category_id"), resultSet.getString("category")));
            c.addFood(food);
            categories.add(c);

            for (Ingredient ingredient : ingredients) {
                if (food.getFoodID() == ingredient.getFoodID()) {
                    food.addIngredient(ingredient);
                }
            }

        }
        categories.forEach(menu::addCat);
        return menu;
    }

}
