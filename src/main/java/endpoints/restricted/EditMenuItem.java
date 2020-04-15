package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import databaseInit.Database;
import entities.Food;
import entities.Ingredient;
import entities.StaffInstance;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



/**
 * Endpoint for the frontend to call to get {@link Food} items from the database to edit.
 *
 * <p>Spec:
 * GET - int: id
 * POST - int: id then one of the two following:
 * - boolean: availability (changes the availability of a food item)
 * - int: calories, int: category, String: description, String: name, BigDecimal: price,
 * String: image (Creates a new food item).
 * Not all parameters need to be passed for the POST request only the ones that are being changed.
 * DELETE - int: id
 *
 * @author Cameron Jones
 */
public class EditMenuItem extends HttpServlet {

  private ObjectMapper om = new ObjectMapper();

  /**
   * Gets the specified menu item.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (!validStaff(req, resp)) {
      return;
    }
    int foodID;
    try {
      foodID = getFoodID(req, resp);
    } catch (NumberFormatException e) {
      return;
    }
    Food menuItem;
    try {
      //TODO Stop calling database here
      menuItem = Database.FOODS.getFoodByID(foodID, true);
    } catch (SQLException e) {
      resp.sendError(500, "Unable to retrieve menu item.");
      return;
    }
    resp.reset();
    resp.setContentType("application/json");
    PrintWriter pw = resp.getWriter();
    pw.println(om.writeValueAsString(menuItem));
    pw.flush();
  }

  /**
   * Writes the updated {@link Food} to the database.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  //TODO Method for completely new food
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (!validStaff(req, resp)) {
      return;
    }
    int foodID;
    try {
      foodID = getFoodID(req, resp);
    } catch (NumberFormatException e) {
      return;
    }
    Food food;
    try {
      food = Database.FOODS.getFoodByID(foodID, true);
      if (food == null) {
        resp.sendError(500, "Unable to retrieve food item to update.");
        return;
      }
    } catch (SQLException e) {
      resp.sendError(500, "Unable to retrieve food item to update.");
      return;
    }
    String availability = req.getParameter("availability");
    if (availability != null) {
      try {
        boolean val = Boolean.parseBoolean(availability);
        Database.FOODS.updateAvailability(foodID, val);
      } catch (SQLException e) {
        resp.sendError(500, "Unable to change availability of food item.");
      }
    } else {
      Map<String, String[]> params = req.getParameterMap();
      for (String param : params.keySet()) {
        try {
          switch (param) {
            case "calories":
              if (params.get(param) != null) {
                int val = Integer.parseInt(params.get(param)[0]);
                //Database.FOODS.updateCalories(foodID, val);
                food.setCalories(val);
              } else {
                resp.sendError(400, "No value passed with: " + param);
                return;
              }
              break;
            case "category":
              if (params.get(param) != null) {
                int val = Integer.parseInt(params.get(param)[0]);
                //Database.FOODS.updateCategoryId(foodID, val);
                food.setCategoryID(val);
              } else {
                resp.sendError(400, "No value passed with: " + param);
                return;
              }
              break;
            case "description":
              if (params.get(param) != null) {
                //Database.FOODS.updateFoodDescription(foodID, params.get(param)[0]);
                food.setFoodDescription(params.get(param)[0]);
              } else {
                resp.sendError(400, "No value passed with: " + param);
                return;
              }
              break;
            case "name":
              if (params.get(param) != null) {
                //Database.FOODS.updateFoodName(foodID, params.get(param)[0]);
                food.setFoodName(params.get(param)[0]);
              } else {
                resp.sendError(400, "No value passed with: " + param);
                return;
              }
              break;
            case "price":
              if (params.get(param) != null) {
                BigDecimal val = new BigDecimal(params.get(param)[0]);
                //Database.FOODS.updatePrice(foodID, val);
                food.setPrice(val);
              } else {
                resp.sendError(400, "No value passed with: " + param);
                return;
              }
              break;
            case "ingredients":
              if (params.get(param) != null) {
                String[] ingredients = params.get(param);
                for (String ingredientID : ingredients) {
                  food.addIngredient(new Ingredient(foodID, Integer.parseInt(ingredientID)));
                }
              } else {
                resp.sendError(400, "No value passed with: " + param);
                return;
              }
              break;
            case "image":
              if (params.get(param) != null) {
                String url = params.get(param)[0];
                food.setImageUrl(url);
              } else {
                resp.sendError(400, "No value passed with: " + param);
                return;
              }
              break;
            case "id":
              break;
            default:
              resp.sendError(400, "Invalid parameter passed: " + param);
              return;
          }
        } catch (NumberFormatException e) {
          resp.sendError(400, "Invalid integer passed for value: " + param);
          return;
        }
      }
      try {
        Database.FOODS.updateAvailability(foodID, false);
      } catch (SQLException e) {
        resp.sendError(500, "Unable to set previous version to unavailable.");
        return;
      }
      try {
        Database.FOODS.newFood(food);
      } catch (SQLException e) {
        resp.sendError(500, "Unable to add new food item.");
      }
    }
  }

  /**
   * Deletes the selected {@link Food} in the database.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @throws IOException If an input or output exception occurs
   */
  @Override
  protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    if (!validStaff(req, resp)) {
      return;
    }
    int foodID;
    try {
      foodID = getFoodID(req, resp);
    } catch (NumberFormatException e) {
      return;
    }
    try {
      Database.FOODS.updateAvailability(foodID, false);
    } catch (SQLException e) {
      resp.sendError(500, "Error occurred whilst changing availability of food.");
    }
  }

  /**
   * Gets the Food ID of the menu item.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @return The food ID of the menu item passed to the backend as stored in the database
   * @throws IOException           IOException If an input or output exception occurs
   * @throws NumberFormatException Thrown when an invalid number is passed by the frontend
   */
  private int getFoodID(HttpServletRequest req, HttpServletResponse resp) throws
          IOException, NumberFormatException {
    int foodID;
    try {
      foodID = Integer.parseInt(req.getParameter("id"));
      if (foodID < 0) {
        resp.sendError(400, "Invalid ID");
      }
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid ID");
      throw new NumberFormatException();
    }
    return foodID;
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
