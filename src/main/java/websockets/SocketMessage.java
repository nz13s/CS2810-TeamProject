package websockets;

import entities.ISerialisable;

/**
 * A class to initialise a socket message.
 *
 * @author Oliver Graham
 */
public class SocketMessage implements ISerialisable {

    ISerialisable content;
    SocketMessageType messageType;

    public SocketMessage(ISerialisable content, SocketMessageType messageType) {
        this.content = content;
        this.messageType = messageType;
    }

}
