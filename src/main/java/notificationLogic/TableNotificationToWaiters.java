package notificationLogic;

import entities.ActiveStaff;
import entities.Notification;
import entities.StaffInstance;
import entities.Table;
import entities.NotificationTypes;

import java.util.List;

/**
 * Class that manages the notifications to waiters if a new order has been checked out by the customer.
 *
 * @author Jatin Khatra
 */

//TODO - REFACTOR WHEN POSSIBLE
public class TableNotificationToWaiters {

    private List<StaffInstance> staff;

    /**
     * Constructor that initialises the staff attribute to hold all active staff.
     */

    public TableNotificationToWaiters() {
        staff = ActiveStaff.getAllActiveStaff();
    }

    /**
     * Adds a table assignment notification to a given staff, by their ID.
     *
     * @param table table to be assigned to the waiter.
     * @param staffID ID of the staff to be assigned a table.
     */

    public void addTableToStaff(Table table, int staffID) {
        for (StaffInstance staffInstance : staff) {
            if (staffInstance.getStaffID() == staffID) {
                staffInstance.addNotification(new Notification(table, NotificationTypes.ASSIGN));
            }
        }
    }
}
