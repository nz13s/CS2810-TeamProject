package filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * A filter for restricted access.
 *
 * @author Oliver Graham
 */
@WebFilter("/restricted/*")
public class RestrictedAccessFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {
    HttpServletResponse resp = (HttpServletResponse) response;
    HttpServletRequest req = (HttpServletRequest) request;
    if (req.getSession().getAttribute("StaffEntity") != null) {
      //will add better/more verification later
      chain.doFilter(request, response);
      return;
    }
    resp.sendError(401);
  }
}
