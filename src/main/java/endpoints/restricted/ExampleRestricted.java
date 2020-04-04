package endpoints.restricted;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ExampleRestricted class
 */

public class ExampleRestricted extends HttpServlet {

    /**
     * Gets a response printing "Access Granted" for authorisation
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        resp.getWriter().println("Access granted.");
        resp.getWriter().flush();
    }
}
