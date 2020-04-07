package endpoints;

import databaseInit.Database;
import entities.ActiveStaff;
import entities.StaffInstance;
import filters.SessionRepositoryRequestWrapper;
import verification.LoginVerification;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Login class handling authorisation for login details
 */
public class Login extends HttpServlet {

    /**
     * Sends POST request to authorise login credentials, containing error handling
     * Validates with database values
     * @param servlet request
     * @param servlet response
     * @throws ServletException if exception occurs within the servlet
     * @throws IOException if input/output error occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //workflow is deny on empty credentials
        //delegate work to sql, so that there is less risk of accidentally introducing a leak

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null
                || username.isEmpty() || password.isEmpty()) {
            resp.sendError(400, "Expected non-null, non-empty username and password fields.");
            return;
        }

        if (!LoginVerification.checkUsername(username)) {
            //bad username
            resp.sendError(400, LoginVerification.getNiceUsernameMessage());
            return;
        }

        int userID = Database.AUTHENTICATION.login(username, password);
        if (userID < 0) { //an error code
            switch (userID) {
                case -1:
                    resp.setStatus(403);
                    resp.getWriter().println("{\"code\":\"Invalid username or password\"}");
                    return;
                case -2:
                    resp.sendError(500, "SQL server error.");
                    return;
                default:
                    resp.sendError(500, "Unknown error.");
                    return;
            }
        } else {
            SessionRepositoryRequestWrapper sessWrapper = (SessionRepositoryRequestWrapper) req;
            HttpSession theSession = sessWrapper.genSession();
            resp.setHeader("X-Session-ID", theSession.getId());
            resp.getWriter().println("{\"userID\":" + userID + "}");

            StaffInstance staff;
            if(!ActiveStaff.isActive(userID)){
                staff = new StaffInstance(userID);
                ActiveStaff.addStaff(staff);
            }else{
                staff = ActiveStaff.getStaffByID(userID);
            }
            theSession.setAttribute("StaffEntity", staff);
        }
    }
}
