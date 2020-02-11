package endpoints.restricted;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.StaffInstance;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Endpoint for the frontend to call to get the notifications for a sessions {@link StaffInstance}
 */
@WebServlet("/staff/notifications")
public class StaffNotifications extends HttpServlet {

    private ObjectMapper om = new ObjectMapper();

    /**
     * Gets the notifications stored for the staff member
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        StaffInstance staff = getStaffMember(req);
        resp.reset();
        resp.setContentType("application/json");
        PrintWriter pw = resp.getWriter();
        pw.println(om.writeValueAsString(staff.getNotifications()));
        pw.flush();
    }

    /**
     * Removes the notification for the sessions {@link StaffInstance} specified by the user input
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int ID;
        try {
            ID = Integer.parseInt(req.getParameter("notificationID"));
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid ID.");
            return;
        }
        StaffInstance staff = getStaffMember(req);
        staff.removeNotification(ID);
    }

    /**
     * Gets the sessions {@link StaffInstance} associated and returns it
     *
     * @param req The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @return The StaffInstance associated with the session
     */
    private StaffInstance getStaffMember(HttpServletRequest req) {
        StaffInstance staff = (StaffInstance) req.getSession().getAttribute("staffMember");
        if (staff == null) {
//            staff = new StaffInstance("Bob");
//            //TODO Throw error and remove create instance since only staff should be able to get here
//            req.getSession().setAttribute("staffMember", staff);
            throw new IllegalStateException("No staff object found.");
        }
        return staff;
    }
}
