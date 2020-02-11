package endpoints;

import com.fasterxml.jackson.databind.ObjectMapper;
import databaseInit.Database;
import entities.Food;
import entities.StaffInstance;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

/**
 * Endpoint for the frontend to call to get {@link Food} items from the database to edit
 */
@WebServlet("/staff/editmenu")
public class EditMenuItem extends HttpServlet {

    private ObjectMapper om = new ObjectMapper();

    /**
     * Gets the specified menu item
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StaffInstance staff = getStaffMember(req);
        if (staff == null) {
            resp.sendError(400, "Not a staff member.");
            return;
        }
        int foodID;
        try{
            foodID = Integer.parseInt(req.getParameter("id"));
        }catch(NumberFormatException e) {
            resp.sendError(400, "Invalid ID");
            return;
        }
        if(foodID < 0){
            resp.sendError(400, "Invalid ID");
        }
        Food menuItem;
        try {
            //TODO Stop calling database here
            menuItem = Database.FOODS.getFoodByID(foodID);
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
     * Writes the updated {@link Food} to the database
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO Get a method from Database people to edit a food item
        //Something for just changing availability?
        //Maybe a variable to check the last called thing for security?
    }

    /**
     * Deletes the selected {@link Food} in the database
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //TODO Get a method from Database people to delete a food item
    }

    /**
     * Gets the sessions {@link StaffInstance} associated and returns it
     *
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The StaffInstance associated with the session
     */
    private StaffInstance getStaffMember(HttpServletRequest req) {
        return (StaffInstance) req.getSession().getAttribute("staffMember");
    }

}
