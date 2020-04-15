package websockets;

import com.fasterxml.jackson.databind.ObjectMapper;
import entities.StaffInstance;
import filters.HttpSessionCollector;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * A class to implement notifications via sockets.
 *
 * @author Oliver Graham
 */
@ServerEndpoint("/sockets/staff")
public class NotificationSocket {

  //Apparently an implied default constructor isn't enough. Yikes.
  public NotificationSocket() {
  }

  static ObjectMapper objectMapper = new ObjectMapper();
  static ScheduledExecutorService exec = Executors.newScheduledThreadPool(2);
  static List<NotificationSocket> registeredStaff = new ArrayList<>();
  static final ByteBuffer pingData = ByteBuffer.wrap("ping".getBytes());

  String httpSessionId = "";
  StaffInstance instance;
  Session session;
  ScheduledFuture<?> pong;


  /**
   * Opens a socket session.
   *
   * @param session of socket
   */
  @OnOpen
  public void onOpen(Session session) {
    System.out.println(session.getRequestParameterMap().entrySet().stream().map(entry ->
            entry.getKey() + " " + entry.getValue()).collect(Collectors.joining("\r\n")));
    List<String> sessions = session.getRequestParameterMap().get("X-Session-ID");
    if (sessions == null || sessions.isEmpty()) {
      try {
        session.getBasicRemote().sendText("Not authenticated. Disconnecting.\n");
        session.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return;
    }
    httpSessionId = sessions.get(0);
    this.session = session;
    register(this);
  }

  /**
   * Closes the session.
   *
   * @param session (unused)
   * @param reason  (unused)
   */
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
    HttpSession sess = HttpSessionCollector.find(socket.httpSessionId);
    if (sess == null) {
      try {
        socket.session.getBasicRemote().sendText("Invalid Session\n");
        socket.session.close();
      } catch (Exception ex) {
        ex.printStackTrace();
      }
      return;
    }
    Object o = sess.getAttribute("StaffEntity");
    if (o == null) {
      try {
        socket.session.getAsyncRemote().sendText("Not authenticated\n");
        socket.session.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
      return;
    }
    socket.instance = (StaffInstance) o;
    registeredStaff.add(socket);
    socket.session.getAsyncRemote().sendText("Auth accepted\n");
    socket.pong = exec.scheduleAtFixedRate(() -> {
      try {
        socket.session.getAsyncRemote().sendPing(pingData);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }, 5, 10, TimeUnit.SECONDS);
  }

  static void unregister(NotificationSocket socket) {
    registeredStaff.remove(socket);
    socket.pong.cancel(true);
  }

  /**
   * Pushes notification to the socket.
   *
   * @param message to be sent
   * @param staff   list
   */
  public static void pushNotification(SocketMessage message, StaffInstance... staff) {
    List<StaffInstance> toPush = Arrays.asList(staff);
    registeredStaff.stream().filter(sock -> toPush.contains(sock.instance)).forEach(
            sock -> {
              try {
                sock.session.getAsyncRemote().sendText(
                        objectMapper.writeValueAsString(message) + "\n");
              } catch (IOException ex) {
                ex.printStackTrace();
              }
            }
    );
  }

  /**
   * Broadcast notification to the socket.
   *
   * @param message to be sent
   */
  public static void broadcastNotification(SocketMessage message) {
    for (NotificationSocket sock : registeredStaff) {
      try {
        sock.session.getAsyncRemote().sendText(objectMapper.writeValueAsString(message) + "\n");
      } catch (IOException ex) {
        ex.printStackTrace();
      }
    }
  }
}
