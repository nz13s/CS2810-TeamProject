package filters;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * A class to track HTTP connections.
 *
 * @author Oliver Graham
 */
@WebListener
public class HttpSessionCollector implements HttpSessionListener {
  private static final Map<String, HttpSession> sessions = new HashMap<String, HttpSession>();

  @Override
  public void sessionCreated(HttpSessionEvent event) {
    HttpSession session = event.getSession();
    sessions.put(session.getId(), session);
  }

  @Override
  public void sessionDestroyed(HttpSessionEvent event) {
    sessions.remove(event.getSession().getId());
  }

  public static HttpSession find(String sessionId) {
    return sessions.get(sessionId);
  }

}
