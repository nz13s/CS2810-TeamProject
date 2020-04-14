package filters;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

/**
 * A class to implement a request wrapper.
 *
 * @author Oliver Graham.
 */
public class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {

  public SessionRepositoryRequestWrapper(HttpServletRequest original) {
    super(original);
  }

  public HttpSession getSession() {
    return getSession(true);
  }

  public HttpSession getSession(boolean createNew) {
    return HttpSessionCollector.find(getHeader("X-Session-ID"));
  }

  public HttpSession genSession() {
    return super.getSession(true);
  }


}
