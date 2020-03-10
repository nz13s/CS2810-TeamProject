package websockets;

import entities.ISerialisable;

public class SocketMessage implements ISerialisable {

    ISerialisable content;
    SocketMessageType messageType;

    public SocketMessage(ISerialisable content, SocketMessageType messageType) {
        this.content = content;
        this.messageType = messageType;
    }

    public ISerialisable getContent() {
        return content;
    }

    public SocketMessageType getMessageType() {
        return messageType;
    }
}
