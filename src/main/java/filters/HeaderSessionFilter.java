package filters;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Filter for header sessions.
 *
 * @author Oliver Graham
 */
public class HeaderSessionFilter implements Filter {

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
          throws IOException, ServletException {

    HttpServletResponse resp = (HttpServletResponse) response;
    HttpServletRequest req = (HttpServletRequest) request;

    if (req.getServletPath().startsWith("/sockets/")) {
      //allow web sockets without headers specified.
      chain.doFilter(request, response);
      return;
    }

    SessionRepositoryRequestWrapper wrapper = new SessionRepositoryRequestWrapper(req);
    HttpSession session = wrapper.getSession();
    if (req.getServletPath().equalsIgnoreCase("/hello")) {
      session = wrapper.genSession();
    } else if (req.getServletPath().equalsIgnoreCase("/login")) {
      chain.doFilter(wrapper, response);
      //delegate the session creation to the login object, if and only if the login was successful.
      return;
    } else {
      if (wrapper.getSession(false) == null) {
        resp.sendError(401, "You need to provide your authentication token via "
                + "the X-Session-ID. You can initiate a session with /hello");
        return;
      }
    }
    resp.setHeader("X-Session-ID", session.getId());
    session.setMaxInactiveInterval(60 * 60 * 24 /* 1 day */);
    chain.doFilter(wrapper, response);
  }
}
