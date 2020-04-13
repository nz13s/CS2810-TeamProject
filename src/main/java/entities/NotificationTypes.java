package entities;

public enum NotificationTypes {

    READY("Order Ready for Delivery"),
    ASSIST("Assistance Required"),
    CONFIRM("Order Ready for Payment"),
    CONFIRMED("Order Has been confirmed"),
    CUSTOM(""),
    ASSIGN("Table number assigned successfully"),
    NEED("Table needs a waiter."),
    PREPARING("Order is being prepared"),
    REMOVE("Table has been removed from your managed tables.");

    //CONFIRMED("Order has been confirmed by the waiter/");

    private String notification;

    NotificationTypes(String notification) {
        this.notification = notification;
    }

    public String toString() {
        return this.notification;
    }

}
