package entities;

public enum NotificationTypes {

    READY("Order Ready for Delivery"),
    ASSIST("Assistance Required"),
    CONFIRM("Order Ready for Payment");

    private String notification;

    private NotificationTypes(String notification){
        this.notification = notification;
    }

    public String toString(){
        return this.notification;
    }

}
