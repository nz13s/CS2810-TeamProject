package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import databaseInit.Database;
import entities.FoodIngredients;
import entities.Ingredient;
import entities.StaffInstance;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;


/**
 * Endpoint for the frontend to call to get {@link Ingredient}'s from the database to edit.
 *
 * <p>Spec:
 * GET - None
 * POST - String: name, boolean: allergen
 *
 * @author Cameron Jones
 */
public class EditIngredients extends HttpServlet {

  private ObjectMapper om = new ObjectMapper();

  /**
   * Gets all the {@link FoodIngredients}'s in the database.
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
    List<FoodIngredients> ingredients = null;
    try {
      //TODO Stop calling database here
      ingredients = Database.FOOD_INGREDIENTS.getAllIngredients();
    } catch (SQLException e) {
      resp.sendError(500, "Unable to retrieve ingredients list.");
      return;
    }
    resp.reset();
    resp.setContentType("application/json");
    PrintWriter pw = resp.getWriter();
    pw.println(om.writeValueAsString(ingredients));
    pw.flush();
  }

  /**
   * Adds a new {@link FoodIngredients} to the database.
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
    String ingredient = req.getParameter("name");
    if (ingredient == null) {
      resp.sendError(400, "No ingredient name passed.");
      return;
    }
    String allergenS = req.getParameter("allergen");
    if (allergenS == null) {
      resp.sendError(400, "No parameter for whether ingredient is an allergen passed.");
      return;
    }
    boolean isAllergen = Boolean.parseBoolean(allergenS);
    try {
      //TODO Stop calling database here
      Database.FOOD_INGREDIENTS.addIngredient(ingredient, isAllergen);
    } catch (SQLException e) {
      resp.sendError(500, "Unable to add new ingredient to the ingredients list.");
      return;
    }

  }

  /**
   * Checks whether the current session is able to change the ingredients list.
   *
   * @param req  The {@link HttpServletRequest} object that contains
   *             the request the client made of the servlet.
   * @param resp The {@link HttpServletResponse} object that contains
   *             the response the servlet returns to the client.
   * @return True if the staff member is able to change the ingredients list, false otherwise
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
