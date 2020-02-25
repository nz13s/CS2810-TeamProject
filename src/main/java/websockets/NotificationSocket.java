package websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.Notification;
import entities.StaffInstance;
import filters.HttpSessionCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/protected/pushnotifications")
public class NotificationSocket {


    static ObjectMapper objectMapper = new ObjectMapper();
    static List<NotificationSocket> registeredStaff = new ArrayList<>();

    String HTTPSessionID = "";
    int tableNum = -1;
    StaffInstance instance;
    Session session;

    @OnOpen
    void onOpen(Session session) {
    }

    @OnClose
    void onClose(Session session) {
        unregister(this);
    }

    @OnMessage
    void onMessage(Session session, String message) {
        this.session = session;
        try {
            if (message.equalsIgnoreCase("bye"))
                session.close();
            else {
                HTTPSessionID = message;
                register(this);
            }
        } catch (IOException e) {
            //problem closing the stream.
            //likely the stream was already closed
        }
    }

    @OnError
    void onError(Session session, Throwable throwable) {
    }


    static void register(NotificationSocket socket) {
        HttpSession sess = HttpSessionCollector.find(socket.HTTPSessionID);
        if (sess == null) {
            socket.session.getAsyncRemote().sendText("Invalid Session\n");
            return;
        }
        Object o = sess.getAttribute("StaffEntitiy");
        if (o == null) {
            socket.session.getAsyncRemote().sendText("Not authenticated\n");
            return;
        }
        socket.instance = (StaffInstance) o;
        registeredStaff.add(socket);

    }

    static void unregister(NotificationSocket socket) {
        registeredStaff.remove(socket);
    }

    static void pushNotification(StaffInstance staff, Notification notification) {
        registeredStaff.stream().filter(sock -> sock.instance == staff).findFirst().ifPresent(sock ->
                {
                    try {
                        sock.session.getAsyncRemote().sendText(objectMapper.writeValueAsString(notification) + "\n");
                    } catch (IOException ignored) {
                    }
                }
        );
    }
}
