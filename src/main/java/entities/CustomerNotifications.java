package entities;

import java.util.ArrayList;

/**
 * Holds a list of all notifications that belong to a customer's order status.
 *
 * @author Jatin Khatra
 */
public class CustomerNotifications {
  private static ArrayList<Notification> notifications;

  public CustomerNotifications() {
    notifications = new ArrayList<>();
  }

  /**
   * Getter for the notifications list.
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

  /**
   * Removes a notification from the list, based on a given ID.
   *
   * @param id ID of the notification to be removed.
   */
  public void removeNotificationByID(int id) {
    notifications.removeIf(notification -> notification.getNotificationID() == id);
  }

  /**
   * Removes all notifications based on a given tableNum.
   *
   * @param tableNum given tableNum to remove all notifications for.
   */
  public void removeNotificationsByTableNum(int tableNum) {
    notifications.removeIf(notification -> notification.getTable().getTableNum() == tableNum);
  }

}
