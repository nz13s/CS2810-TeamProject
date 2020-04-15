package filters;

import databaseInit.Database;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A class to filter database connections.
 *
 * @author Oliver Graham
 */
public class DatabaseConnectionFilter implements Filter {

  private String cachedError;
  private boolean checked = false;

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    HttpServletResponse resp = (HttpServletResponse) response;
    HttpServletRequest req = (HttpServletRequest) request;

    if (req.getMethod().equals("OPTIONS")) {
      resp.setHeader("Allow", "POST, GET, HEAD, OPTIONS, DELETE");
      resp.setHeader("Access-Control-Allow-Methods", "POST, GET, HEAD, OPTIONS, DELETE");
      return;
    }

    if (cachedError == null) {
      if (!checked) {
        SQLException ex = Database.getException();
        if (ex == null) {
          checked = true;
        } else {
          StringWriter stringWriter = new StringWriter();
          ex.printStackTrace(new PrintWriter(stringWriter));
          stringWriter.flush();
          cachedError = stringWriter.toString();
          stringWriter.close();
        }
      }
    }

    if (cachedError != null) {
      resp.sendError(500, cachedError);
      return;
    }

    chain.doFilter(request, response);

  }
}
