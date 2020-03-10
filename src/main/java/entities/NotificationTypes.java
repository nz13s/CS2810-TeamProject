package entities;

public enum NotificationTypes {

    READY("Order Ready for Delivery"),
    ASSIST("Assistance Required"),
    CONFIRM("Order Ready for Payment"),
    CUSTOM(""),
    ASSIGN("Table number assigned successfully"),
    NEED("Table needs a waiter.");

    private String notification;

    NotificationTypes(String notification) {
        this.notification = notification;
    }

    public String toString() {
        return this.notification;
    }

}
