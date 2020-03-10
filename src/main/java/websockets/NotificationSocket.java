package websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Notification;
import entities.StaffInstance;
import filters.HttpSessionCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.naming.PartialResultException;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/sockets/staff")
public class NotificationSocket {

    //apparently an implied default constructor isn't enough. Yikes.
    public NotificationSocket() {
    }

    static ObjectMapper objectMapper = new ObjectMapper();
    static List<NotificationSocket> registeredStaff = new ArrayList<>();

    String HTTPSessionID = "";
    StaffInstance instance;
    Session session;

    @OnOpen
    public void onOpen(Session session) {
        System.out.println(session.getRequestParameterMap().entrySet().stream().map(entry -> entry.getKey() + " " + entry.getValue()).collect(Collectors.joining("\r\n")));
        List<String> sessions = session.getRequestParameterMap().get("X-Session-ID");
        if (sessions == null || sessions.isEmpty()) {
            try {
                session.getBasicRemote().sendText("Not authenticated. Disconnecting.\n");
                session.close();
            } catch (IOException e) {
            }
            return;
        }
        HTTPSessionID = sessions.get(0);
        this.session = session;
        register(this);
    }

    @OnClose
    public void onClose(Session session, CloseReason reason) {
        unregister(this);
    }

    @OnMessage
    public void onMessage(Session session, String message) {
        session.getAsyncRemote().sendText(message); //simple echo to keepalive
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.out.println(throwable.getMessage());
    }


    static void register(NotificationSocket socket) {
        HttpSession sess = HttpSessionCollector.find(socket.HTTPSessionID);
        if (sess == null) {
            try {
                socket.session.getBasicRemote().sendText("Invalid Session\n");
                socket.session.close();
            } catch (Exception ex) {
            }
            return;
        }
        Object o = sess.getAttribute("StaffEntity");
        if (o == null) {
            try {
                socket.session.getAsyncRemote().sendText("Not authenticated\n");
                socket.session.close();
            } catch (IOException e) {
            }
            return;
        }
        socket.instance = (StaffInstance) o;
        registeredStaff.add(socket);
        socket.session.getAsyncRemote().sendText("Auth accepted\n");

    }

    static void unregister(NotificationSocket socket) {
        registeredStaff.remove(socket);
    }

    public static void pushNotification(SocketMessage message, StaffInstance... staff) {
        List<StaffInstance> toPush = Arrays.asList(staff);
        registeredStaff.stream().filter(sock -> toPush.contains(sock.instance)).forEach(sock ->
                {
                    try {
                        sock.session.getAsyncRemote().sendText(objectMapper.writeValueAsString(message) + "\n");
                    } catch (IOException ignored) {
                    }
                }
        );
    }

    public static void broadcastNotification(SocketMessage message) {
        registeredStaff.forEach(sock ->
                {
                    try {
                        sock.session.getAsyncRemote().sendText(objectMapper.writeValueAsString(message) + "\n");
                    } catch (IOException ignored) {
                    }
                }
        );
    }
}
