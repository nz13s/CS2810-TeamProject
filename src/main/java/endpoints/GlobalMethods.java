package endpoints;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GlobalMethods {
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
