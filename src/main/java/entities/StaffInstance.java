package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single staff member.
 *
 * @author Cameron Jones, Oliver Graham, Bhavik Narang, Jatin Khatra, Tony Delchev
 */
public class StaffInstance {
  private List<Notification> allMessages;
  private int staffID;
  private static List<Table> managedTables;

  /**
   * Creates a new staff instance for a session used by a staff member.
   *
   * @param staffID The ID of the staff member the session is created by
   */
  public StaffInstance(int staffID) {
    this.staffID = staffID;
    allMessages = new ArrayList<>();
    managedTables = new ArrayList<Table>();
  }

  /**
   * Removes the notification with the specified ID the notifications list.
   *
   * @param id The ID of the notification to remove
   */
  public void removeNotificationByID(int id) {
    allMessages.removeIf(notification -> notification.getNotificationID() == id);
  }

  /**
   * Gets notification by its ID.
   */
  public Notification getNotificationById(int notificationId) {
    return allMessages.stream().filter(msg ->
            msg.getNotificationID() == notificationId).findFirst().orElse(null);
  }

  /**
   * Adds a new notification to the all notification queue for the staff member.
   *
   * @param newNotification A notification for the staff member
   */
  public void addNotification(Notification newNotification) {
    allMessages.add(newNotification);
  }

  /**
   * Gets all the notifications for the staff member.
   *
   * @return The stored list of messages for the staff member
   */
  public List<Notification> getNotifications() {
    return this.allMessages;
  }

  /**
   * Adds a new table to the staff member to look after.
   *
   * @param newTable The new table that the staff member is looking after
   */
  public void addTable(Table newTable) {
    managedTables.add(newTable);
  }

  /**
   * Removes the table from the staff members list of tables to look after.
   *
   * @param tableToRemove The table to remove from the staff members list of tables
   */
  public void removeTable(Table tableToRemove) {
    managedTables.remove(tableToRemove);
  }

  /**
   * Gets the specified table by table number, returns null if the table is not in
   * the staff members list of tables.
   *
   * @param tableNum The number of the table to get
   * @return The table specified by the table number, null if the staff member is not in
   * charge of that table.
   */
  public Table getTable(int tableNum) {
    return managedTables.stream().filter(table ->
            table.tableNum == tableNum).findFirst().orElse(null);
  }

  /**
   * Check if a Table is in the list of managed tables.
   *
   * @param newTable to be checked
   * @return true if exists in list, otherwise false.
   */
  public boolean hasTable(Table newTable) {
    if (!managedTables.isEmpty()) {
      for (Table t : managedTables) {
        if (t.tableNum == newTable.tableNum) {
          return true;
        }
      }
    }
    return false;
  }

  public static List<Table> getTables() {
    return managedTables;
  }

  /**
   * Returns the staffs ID as stored in the database.
   *
   * @return The String identifier for the staff member
   */
  public int getStaffID() {
    return this.staffID;
  }

  /**
   * Returns a Notification object from a given message from all notifications list.
   *
   * @param notificationID ID of the notification.
   * @return Notification based on ID.
   */

  public Notification getNotificationByID(int notificationID) {
    for (Notification allMessage : allMessages) {
      if (allMessage.getNotificationID() == notificationID) {
        return allMessage;
      }
    }
    return null;
  }
}