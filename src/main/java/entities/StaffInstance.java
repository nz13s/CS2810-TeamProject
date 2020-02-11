package entities;

import java.util.LinkedList;

public class StaffInstance {

    //TODO Ask about this being a database thing for persistence/loss of connection?
    private LinkedList<String> messages;//TODO Discuss making a notification class
    private int staffID;
    private LinkedList<Table> tables;//TODO Remove if other method is decided better

    /**
     * Creates a new staff instance for a session used by a staff member
     *
     * @param staffID The ID of the staff member the session is created by
     */
    public StaffInstance(int staffID) {
        this.staffID = staffID;
        messages = new LinkedList<>();
    }

    /**
     * Removes the notification at the specified index
     *
     * @param pos The index of the notification to remove
     */
    public void removeNotification(int pos) {
        if (pos >= 0 && pos < messages.size()) {
            messages.remove(pos);
        }
    }

    /**
     * Adds a new notification to the notification queue for the staff member
     *
     * @param newNotification A String notification fot he staff member
     */
    public void addNotification(String newNotification) {
        messages.add(newNotification);
    }

    /**
     * Gets the notifications for the staff member
     *
     * @return The stored list of messages for the staff member
     */
    public LinkedList<String> getNotifications() {
        return this.messages;
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
}
