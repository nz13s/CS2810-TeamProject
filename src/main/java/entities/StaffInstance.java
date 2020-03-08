package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * This class represents a single staff member.
 */
public class StaffInstance {

    //TODO Ask about this being a database thing for persistence/loss of connection?
    private List<Notification> allMessages;//TODO Discuss making a notification class
    private List<Notification> activeMessages;
    private int staffID;
    private List<Table> managedTables;//TODO Remove if other method is decided better

    /**
     * Creates a new staff instance for a session used by a staff member
     *
     * @param staffID The ID of the staff member the session is created by
     */
    public StaffInstance(int staffID) {
        this.staffID = staffID;
        allMessages = new ArrayList<>();
    }

    /**
     * Removes the notification at the specified index from the all notifications list
     *
     * @param pos The index of the notification to remove
     */
    public void removeNotificationByPos(int pos) {
        if (pos >= 0 && pos < allMessages.size()) {
            allMessages.remove(pos);
        }
    }

    /**
     * Removes the notification with the specified ID the notifications list
     *
     * @param id The ID of the notification to remove
     */
    public void removeNotificationByID(int id) {
        allMessages.removeIf(notification -> notification.getNotificationID() == id);
    }

    /**
     * Gets notification by its ID
     */
    public Notification getNotificationById(int notificationId) {
        return allMessages.stream().filter(msg -> msg.getNotificationID() == notificationId).findFirst().orElse(null);
    }

    /**
     * Removes the notification from the active notification list
     *
     * @param pos The index of the notification to remove
     */
    public void removeActiveNotification(int pos) {
        if (pos >= 0 && pos < activeMessages.size()) {
            activeMessages.remove(pos);
        }
    }

    /**
     * Adds a new notification to the all notification queue for the staff member
     *
     * @param newNotification A notification for the staff member
     */
    public void addNotification(Notification newNotification) {
        allMessages.add(newNotification);
    }

    /**
     * Adds a new notification to the active notification queue for the staff member
     *
     * @param newNotification A notification for the staff member
     */
    public void addActiveNotification(Notification newNotification) {
        activeMessages.add(newNotification);
    }

    /**
     * Gets all the notifications for the staff member
     *
     * @return The stored list of messages for the staff member
     */
    public List<Notification> getNotifications() {
        return this.allMessages;
    }

    /**
     * Gets the active notifications for the staff member
     *
     * @return The stored list of messages for the staff member
     */
    public List<Notification> getActiveNotifications() {
        return this.activeMessages;
    }

    /**
     * Adds a new table to the staff member to look after
     *
     * @param newTable The new table that the staff member is looking after
     */
    public void addTable(Table newTable) {
        this.managedTables.add(newTable);
    }

    /**
     * Removes the table from the staff members list of tables to look after
     *
     * @param tableToRemove The table to remove from the staff members list of tables
     */
    public void removeTable(Table tableToRemove) {
        this.managedTables.remove(tableToRemove);
    }

    /**
     * Gets the specified table by table number, returns null if the table is not in the staff members list of tables
     *
     * @param tableNum The number of the table to get
     * @return The table specified by the table number, null if the staff member is not in charge of that table
     */
    public Table getTable(int tableNum) {
        return managedTables.stream().filter(table -> table.tableNum == tableNum).findFirst().orElse(null);
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
     * Returns a Notification object from a given message from all notifications list
     *
     * @param message message of the notification.
     * @return Notification based on the message.
     */
    public Notification getNotificationFromMessage(String message) {
        for (Notification notification : allMessages) {
            if (notification.getMessage().equals(message)) {
                return notification;
            }
        }
        return null;
    }

    /**
     * Returns a Notification object from a given message from the active messages list
     *
     * @param message message of the notification.
     * @return Notification based on the message.
     */
    public Notification getNotificationFromActiveMessage(String message) {
        for (Notification notification : allMessages) {
            if (notification.getMessage().equals(message)) {
                return notification;
            }
        }
        return null;
    }

    /**
     * Removes a notification based on a given message from the all messages list.
     *
     * @param message message of the notification.
     */
    public void removeNotificationFromMessage(String message) {

        for (int i = 0; i< allMessages.size(); i++) {
            if (allMessages.get(i).getMessage().equals(message)) {
                allMessages.remove(allMessages.get(i));
            }
        }
    }

    /**
     * Removes a notification based on a given message from the active messages list.
     *
     * @param message message of the notification.
     */

    public void removeNotificationFromActiveMessage(String message) {

        for (int i = 0; i < activeMessages.size(); i++) {
            if (activeMessages.get(i).getMessage().equals(message)) {
                activeMessages.remove(activeMessages.get(i));
            }
        }
    }

    public List<Table> getManagedTables() {
        return managedTables;
    }
}
