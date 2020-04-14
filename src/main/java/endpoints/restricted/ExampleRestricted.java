package endpoints.restricted;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ExampleRestricted class for when accessing restricted information.
 *
 * @author Oliver Graham
 */

public class ExampleRestricted extends HttpServlet {

  /**
   * Gets a response printing "Access Granted" for authorisation.
   *
   * @param req  servlet request
   * @param resp servlet response
   * @throws IOException if input/output error occurs
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {

    resp.getWriter().println("Access granted.");
    resp.getWriter().flush();
  }
}
