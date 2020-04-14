package endpoints;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Hello class which gets new session.
 *
 * @author Oliver Graham
 */
public class Hello extends HttpServlet {

  /**
   * Gets a valid X-session-ID after sending request.
   *
   * @param req  servlet request
   * @param resp servlet response
   */
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
    req.getSession(true);
  }
}
