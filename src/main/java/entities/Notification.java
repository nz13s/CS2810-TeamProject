package entities;

public class Notification {

    private static int counter = 0;
    private int notificationID;
    private NotificationTypes type;
    private Table table;
    private String message;
    private Long time;
    private Boolean completed;

    /**
     * Constructor for an Notification used in the {@link StaffInstance}.
     *
     * @param t    The table of the notification
     * @param type The type of notification
     */
    public Notification(Table t, NotificationTypes type) {
        this.table = t;
        this.message = type.toString();
        this.time = System.currentTimeMillis();
        this.completed = false;
        notificationID = counter++;
    }

    /**
     * Constructor for an Notification used in the {@link StaffInstance} USED FOR DEBUG ONLY.
     *
     * @param message The message to be shown
     */
    public Notification(String message) {
        this.table = null;
        this.message = message;
        this.time = System.currentTimeMillis();
        this.completed = false;
        notificationID = counter++;
    }

    /**
     * Get the table to attend.
     *
     * @return The table
     */
    public Table getTable() {
        return table;
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
     * Get the time of notification.
     *
     * @return time the time
     */
    public Long getTime() {
        return time;
    }

    /**
     * Change the time of notification.
     *
     * @param time new time
     */
    public void setTime(Long time) {
        this.time = time;
    }

    /**
     * Get the notification message.
     *
     * @return message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Sets the message of notification.
     *
     * @param message the new message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Get the status of notification.
     *
     * @return status
     */
    public Boolean getCompleted() {
        return completed;
    }

    /**
     * Sets the status of notification.
     *
     * @param completed status
     */
    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    /**
     * Get the notificationID.
     *
     * @return notificationID
     */
    public int getNotificationID(){ return notificationID; }

    /**
     * Sets the int of notificationID.
     *
     * @param notificationID int
     */
    public void setNotificationID(int notificationID){ this.notificationID = notificationID; }

    /**
     * Gets the object's notification type.
     *
     * @return the type of the notification.
     */
    public NotificationTypes getType() {
        return type;
    }

    /**
     * Sets the object's notification type.
     *
     * @param type type of the notification that is to be set.
     */
    public void setType(NotificationTypes type) {
        this.type = type;
    }
}
