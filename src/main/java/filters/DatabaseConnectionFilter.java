package filters;

import databaseInit.Database;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.SQLException;

@WebFilter("/*")
public class DatabaseConnectionFilter implements Filter {

    private String cachedError;
    private boolean checked = false;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
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
        if (cachedError != null){
            HttpServletResponse resp = (HttpServletResponse) response;
            resp.sendError(500, cachedError);
            return;
        }
        chain.doFilter(request, response);
    }
}
