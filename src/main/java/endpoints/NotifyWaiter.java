//TODO -- Tony look at this pls

package endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import entities.Notification;
import entities.Table;
import entities.serialisers.NotificationSerialiser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/notification") //todo -- what goes in brackets?
public class NotifyWaiter extends HttpServlet {
    private ObjectMapper om = new ObjectMapper();

    @Override
    public void init() throws ServletException {
        om.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module =
                new SimpleModule("NotificationSerialiser",
                        new Version(1,
                                0,
                                0,
                                null,
                                null,
                                null));
        module.addSerializer(Notification.class, new NotificationSerialiser());
        om.registerModule(module);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Notification notification = null;
        boolean success = false;
        Table table = null;
        String s = null;
    }

    private int getTable(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int tableID = -1;
        try {
            tableID = Integer.parseInt(req.getParameter("tableID"));
            if (tableID < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid State integer.");
        }
        return tableID;
    }
}
