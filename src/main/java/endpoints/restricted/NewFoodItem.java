package endpoints.restricted;

import databaseInit.Database;
import entities.Food;
import entities.Ingredient;
import entities.StaffInstance;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Endpoint for the frontend to call to add a new {@link Food} items to the database.
 *
 * <p>Spec:
 * POST - String: name, int: calories, int: category, String: description, BigDecimal: price,
 * List(Int): ingredients, String: image (Creates a new food item).
 *
 * @author Cameron Jones
 */
public class NewFoodItem extends HttpServlet {

  /**
   * Writes a new {@link Food} to the database.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (!validStaff(req, resp)) {
      return;
    }
    String foodName = req.getParameter("name");
    int calories = Integer.parseInt(req.getParameter("calories"));
    int category = Integer.parseInt(req.getParameter("category"));
    String description = req.getParameter("description");
    BigDecimal price = new BigDecimal(req.getParameter("price"));
    ArrayList<Ingredient> ingredients = new ArrayList<>();
    String ingred = req.getParameter("ingredients");
    if (ingred != null) {
      for (String ingredientID : ingred.split(",")) {
        ingredients.add(new Ingredient(-1, Integer.parseInt(ingredientID)));
      }
    }
    String image = req.getParameter("image");
    Food food = new Food(foodName, description, calories, price,
            category, ingredients, image);
    try {
      Database.FOODS.newFood(food);
    } catch (SQLException e) {
      resp.sendError(500, "Unable to add new food item.");
    }
  }

  /**
   * Checks whether the current session is able to change the menu.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @return True if the staff member is able to change the menu, false otherwise
   * @throws IOException If an input or output exception occurs
   */
  private boolean validStaff(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    StaffInstance staff = getStaffMember(req);
    if (staff == null) {
      resp.sendError(400, "Not a staff member.");
      return false;
    }
    return true;
  }

  /**
   * Gets the sessions {@link StaffInstance} associated and returns it.
   *
   * @param req The {@link HttpServletRequest} object that contains
   *            the request the client made of the servlet.
   * @return The StaffInstance associated with the session
   */
  private StaffInstance getStaffMember(HttpServletRequest req) {
    return (StaffInstance) req.getSession().getAttribute("StaffEntity");
  }
}
