package websockets;

public enum SocketMessageType {

    UPDATE(0) /*for updating an already existing order */,
    CREATE(1) /*for creating a new notification or new order */,
    DELETE(2) /*for clearing a notification or order */;

    int typeNum;

    SocketMessageType(int type) {
        this.typeNum = type;
    }

    public int getTypeNum() {
        return typeNum;
    }
}
