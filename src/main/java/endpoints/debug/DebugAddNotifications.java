package endpoints.debug;

import entities.ActiveStaff;
import entities.Notification;
import entities.StaffInstance;
import websockets.NotificationSocket;
import websockets.SocketMessage;
import websockets.SocketMessageType;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Endpoint for the frontend to call to add notifications to the active {@link StaffInstance}
 *
 * Spec:
 *  POST - int: staffID, String: message
 */
public class DebugAddNotifications extends HttpServlet {

    /**
     * Sets a new {@link Notification} for a {@link StaffInstance} by their ID if they are in the {@link ActiveStaff}
     *
     * @param req  The {@link HttpServletRequest} object that contains the request the client made of the servlet
     * @param resp The {@link HttpServletResponse} object that contains the response the servlet returns to the client
     * @throws ServletException
     * @throws IOException      If an input or output exception occurs
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int ID;
        try {
            ID = Integer.parseInt(req.getParameter("staffID"));
            if (ID < 0) {
                resp.sendError(400, "Invalid ID.");
                return;
            }
        } catch (NumberFormatException e) {
            resp.sendError(400, "Invalid ID.");
            return;
        }
        String message = req.getParameter("message");
        if (message == null) {
            resp.sendError(400, "No message for Notification.");
            return;
        }
        Notification toSend = new Notification(message);
        ActiveStaff.addNotification(ID, toSend);
        NotificationSocket.pushNotification(new SocketMessage(toSend, SocketMessageType.CREATE), ActiveStaff.getStaffByID(ID));
    }
}
