package entities;

import entities.Notification;

import java.util.ArrayList;

/**
 * Holds a list of all notifications that belong to a customer's order status.
 *
 * @author Jatin Khatra
 */

public class CustomerNotifications {

    private static ArrayList<Notification> notifications;

    public CustomerNotifications() {
        notifications = new ArrayList<Notification>();
    }

    /**
     * Getter for the notifications list
     *
     * @return notifications list.
     */

    public ArrayList<Notification> getNotifications() {
        return notifications;
    }

    /**
     * Adds a notification object to the list.
     *
     * @param notification the notification to be added to the list.
     */

    public void addNotification(Notification notification) {
        notifications.add(notification);
    }

}
