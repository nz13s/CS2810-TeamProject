package entities;

import java.util.ArrayList;
import java.util.List;

public class StaffInstance {

    //TODO Ask about this being a database thing for persistence/loss of connection?
    private List<Notification> allMessages;//TODO Discuss making a notification class
    private int staffID;
    private List<Table> tables;//TODO Remove if other method is decided better

    /**
     * Creates a new staff instance for a session used by a staff member
     *
     * @param staffID The ID of the staff member the session is created by
     */
    public StaffInstance(int staffID) {
        this.staffID = staffID;
        allMessages = new ArrayList<>();
        tables = new ArrayList<Table>();
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
     * Adds a new notification to the all notification queue for the staff member
     *
     * @param newNotification A notification for the staff member
     */
    public void addNotification(Notification newNotification) {
        allMessages.add(newNotification);
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

    public boolean hasTable(Table newTable) {
        for (Table t : tables) {
            if (t == newTable) {
                return true;
            }
        }
        return false;
    }

    public List<Table> getTables() {
        return tables;
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
     * Returns a notification based on the notificationID.
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

    /**
     * Returns a notification based on the type of a notification.
     *
     * @param type the notification's type.
     * @return notification object based on the type given.
     */

    public Notification getNotificationByEnum(NotificationTypes type) {
        for (Notification allMessage : allMessages) {
            if (allMessage.getType() == type) {
                return allMessage;
            }
        }
        return null;
    }

    /**
     * Removes a notification based on a given type from the all messages list.
     *
     * @param type type of the notification.
     */
    public void removeNotificationFromType(NotificationTypes type) {
        allMessages.removeIf(notification -> notification.getType() == type);
    }

}
