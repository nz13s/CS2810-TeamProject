package endpoints;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Hello class, which gets new session
 */
public class Hello extends HttpServlet {

    /**
     * Gets a valid X-session-ID after sending request
     * @param servlet request
     * @param servlet response
     * @throws ServletException
     * @throws IOException, if input/output error occurs
     * @throws ServletException, if exception occurs within the servlet
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession(true);
    }
}
