package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Notification;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

/**
 * Endpoint that handles notifications related to customer's order tracking.
 *
 * @author Jatin Khatra
 */

//TODO - POST METHOD AND DELETE METHOD.

public class CustomerNotifications extends HttpServlet {

    private ObjectMapper mapper;
    private entities.CustomerNotifications cNotifications;

    /**
     * Initialises attributes.
     */

    public CustomerNotifications() {
        mapper = new ObjectMapper();
        cNotifications = new entities.CustomerNotifications();
    }

    /**
     * GETs all notifications based on a given tableNum.
     *
     * @param req servlet request.
     * @param resp servlet response.
     * @throws ServletException
     * @throws IOException if input/output error occurs.
     */

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ArrayList<Notification> list = getNotificationsByTableNum(req,resp);
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        pw.println(mapper.writeValueAsString(list));
        pw.flush();
    }

    /**
     * Method that gets all notifications for a given table number, and adds it to a list.
     *
     * @param req servlet request.
     * @param resp servlet response.
     * @return list of all notifications for a given table number.
     * @throws IOException if input/output error occurs.
     */

    private ArrayList<Notification> getNotificationsByTableNum(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        int tableNum = -1;

        try {
            tableNum = Integer.parseInt(req.getParameter("tableNum"));
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid table number.");
        }

        ArrayList<Notification> list = new ArrayList<>();

        for(int i=0; i<cNotifications.getNotifications().size(); i++) {
            if (cNotifications.getNotifications().get(i).getTable().getTableNum() == tableNum) {
                list.add(cNotifications.getNotifications().get(i));
            }
        }

        return list;
    }

}
