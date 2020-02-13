package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Class used to deal with notification handling of staff members.
 *
 * @author Tony
 * @author Jatin Khatra
 * @author Bhavik Narang
 */
public class StaffInstance {

    //TODO Ask about this being a database thing for persistence/loss of connection?
    private List<Notification> messages;//TODO Discuss making a notification class
    private List<Notification> activeJobMessages;
    private int staffID;
    private List<Table> tables;//TODO Remove if other method is decided better

    /**
     * Creates a new staff instance for a session used by a staff member
     *
     * @param staffID The ID of the staff member the session is created by
     */
    public StaffInstance(int staffID) {
        this.staffID = staffID;
        messages = new ArrayList<>();
    }

    /**
     * Removes the notification at the specified index from a given list.
     *
     * @param pos The index of the notification to remove
     */
    public void removeNotification(List<Notification> list, int pos) {
        if (pos >= 0 && pos < list.size()) {
            list.remove(pos);
        }
    }

    /**
     * Adds a new notification to the given notification queue for the staff member
     *
     * @param newNotification A notification fot he staff member
     */
    public void addNotification(List<Notification> list, Notification newNotification) {
        list.add(newNotification);
    }

    /**
     * Gets the notifications for the staff member
     *
     * @return The stored list of messages for the staff member
     */
    public List<Notification> getNotifications() {
        return this.messages;
    }

    /**
     * Gets the active notifications for the staff member that they are working on currently
     *
     * @return The stored list of active messages for the staff member
     */
    public List<Notification> getActiveNotifications() {
        return this.activeJobMessages;
    }

    /**
     * Adds a new table to the staff member to look after
     *
     * @param newTable The new table that the staff member is looking after
     */
    public void addTable(Table newTable) {
        this.tables.add(newTable);
    }

    /**
     * Removes the table from the staff members list of tables to look after
     *
     * @param tableToRemove The table to remove from the staff members list of tables
     */
    public void removeTable(Table tableToRemove) {
        this.tables.remove(tableToRemove);
    }

    /**
     * Gets the specified table by table number, returns null if the table is not in the staff members list of tables
     *
     * @param tableNum The number of the table to get
     * @return The table specified by the table number, null if the staff member is not in charge of that table
     */
    public Table getTable(int tableNum) {
        for (Table table : tables) {
            if (table.getTableNum() == tableNum) {
                return table;
            }
        }
        return null;
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
     * Returns a Notification object from a given message from a given list.
     *
     * @param message message of the notification.
     * @return Notification based on the message.
     */
    public Notification getNotificationFromMessage(List<Notification> list, String message) {
        for (Notification notification : list) {
            if (notification.getMessage().equals(message)) {
                return notification;
            }
        }
        return null;
    }

    /**
     * Removes a notification based on a given message from a given list.
     *
     * @param message message of the notification.
     */
    public void removeNotificationFromMessage(List<Notification> list, String message) {
        for (int i=0; i<list.size(); i++) {
            if (list.get(i).getMessage().equals(message)) {
                list.remove(list.get(i));
            }
        }
    }
}