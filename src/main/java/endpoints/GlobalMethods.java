package endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 * GlobalMethods class that stores methods that are used multiple times.
 *
 * @author Tony Delchev
 */
public class GlobalMethods {

  /**
   * Gets the tableID parameter from frontend and returns it.
   *
   * @param req  servlet request
   * @param resp servlet response
   * @return tableID of the table
   * @throws IOException if input/output error occurs
   */
  public static int getTable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    int tableID = -1;
    try {
      tableID = Integer.parseInt(req.getParameter("tableID"));
      if (tableID < 0) {
        throw new NumberFormatException();
      }
    } catch (NumberFormatException e) {
      resp.sendError(400, "Invalid tableID.");
    }
    return tableID;
  }
}
