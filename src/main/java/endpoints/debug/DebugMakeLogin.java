package endpoints.debug;

import databaseInit.Database;
import verification.LoginVerification;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DebugMakeLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            resp.sendError(400, "Must specify a non-empty username and password");
            return;
        }

        if (!LoginVerification.checkUsername(username)) {
            resp.sendError(400, LoginVerification.getNiceUsernameMessage());
            return;
        }

        int id = Database.AUTHENTICATION.addNewUser(username, password);
        resp.getWriter().println("status code: " + id);

    }
}
