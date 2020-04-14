package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A class to set up a new Notification for waiter screen.
 *
 * @author Tony Delchev, Oliver Graham
 */
public class Notification implements ISerialisable {

  private static int counter = 0;
  private int notificationID;
  private NotificationTypes type;
  private Table table;
  private Long time;
  private Boolean completed;
  private Object extraData;

  private int orderID = -1;

  /**
   * Constructor for an Notification used in the {@link StaffInstance}.
   *
   * @param t    The table of the notification
   * @param type The type of notification
   */
  public Notification(Table t, NotificationTypes type) {
    this.type = type;
    this.table = t;
    this.time = System.currentTimeMillis();
    this.completed = false;
    notificationID = counter++;
  }

  /**
   * Constructor for an Notification used in the {@link StaffInstance}.
   *
   * @param t    The table of the notification
   * @param type The type of notification
   */
  public Notification(Table t, NotificationTypes type, int orderID) {
    this(t, type);
    if (type != NotificationTypes.CONFIRM && type != NotificationTypes.CONFIRMED) {
      throw new IllegalStateException("orderID given for a non-confirming notif");
    }
    this.orderID = orderID;
  }

  /**
   * Constructor for an Notification used in the {@link StaffInstance} USED FOR DEBUG ONLY.
   *
   * @param message The message to be shown
   */
  public Notification(String message) {
    this.type = NotificationTypes.CUSTOM;
    this.table = null;
    this.time = System.currentTimeMillis();
    this.completed = false;
    notificationID = counter++;
  }

  /**
   * Get the table to attend.
   *
   * @return The table
   */
  @JsonIgnore //otherwise we end up with infinite recursion
  public Table getTable() {
    return table;
  }

  /**
   * Get the tableID for Jackson so that it doesn't have infinite recursion.
   *
   * @return the tableNum
   */
  public int getTableNum() {
    return table.getTableNum();
  }

  /**
   * Set new table to attend.
   *
   * @param t new table
   */
  public void setTable(Table t) {
    this.table = t;
  }

  /**
   * Get the notificationID.
   *
   * @return notificationID
   */
  public int getNotificationID() {
    return notificationID;
  }

  /**
   * Get the type of notification.
   *
   * @return the notification type
   */
  public NotificationTypes getType() {
    return type;
  }


  public void setExtraData(Object extraData) {
    this.extraData = extraData;
  }

}
