package filters;

import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

public class SessionRepositoryRequestWrapper extends HttpServletRequestWrapper {

    public SessionRepositoryRequestWrapper(HttpServletRequest original) {
        super(original);
    }

    public HttpSession getSession() {
        return getSession(true);
    }

    public HttpSession genSession(){
        return super.getSession(true);
    }

    public HttpSession getSession(boolean createNew) {
        return HttpSessionCollector.find(getHeader("X-Session-ID"));
    }

}
