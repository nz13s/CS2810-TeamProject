package entities;

public enum AllNotifications {

    READY("Order Ready for Delivery"),
    ASSIST("Assistance Required"),
    CONFIRM("Order Ready for Payment");

    private String notification;

    private AllNotifications(String notification){
        this.notification = notification;
    }

    public String toString(){
        return this.notification;
    }

}
