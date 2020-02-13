package entities;

public class Notification {

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
    public Notification(Table t, String type) {
        this.table = t;
        switch (type) {
            case "Ready":
                this.message = "Order Ready for Delivery";
                break;
            case "Assist":
                this.message = "Assistance Required";
                break;
            case "Confirm":
                this.message = "Order Ready for Payment";
                break;
            default:
                this.message = type;
        }
        this.time = System.currentTimeMillis();
        this.completed = false;
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

}
