package endpoints;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import entities.Notification;
import entities.serialisers.NotificationSerialiser;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet() //todo -- what goes in brackets?
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
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Notification> notifications = getNotifications(req, resp);
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        pw.println(om.writeValueAsString(notifications));
        pw.flush();
    }

    private List<Notification> getNotifications(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Notification> notifications;
        try {
            notifications = (List<Notification>) req.getSession().getAttribute("notifications");
            if (notifications.equals(null)) {
                resp.sendError(400, "Nothing found");
            }
        } catch (NumberFormatException | IOException e) {
            resp.sendError(400, "Nothing found");
            throw new NumberFormatException();
        }
        return notifications;
    }
}
