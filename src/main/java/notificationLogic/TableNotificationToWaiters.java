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
     * Adds table notifications to all waiter's notifications.
     *
     * @param table table to be assigned to the new notification.
     */

    public void addTableNotificationToAllStaff(Table table) {
        for (StaffInstance staffInstance : staff) {
            staffInstance.addNotification(new Notification(table, NotificationTypes.CONFIRM));
        }
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
                staffInstance.addActiveNotification(new Notification(table, NotificationTypes.ASSIGN));
            }
        }
    }

    /**
     * Staff accepts the notification and is assigned to the table.
     *
     * @param staffID ID of the staff that accepts the notification.
     */

    public void acceptNotification(int staffID) {
        for (StaffInstance staffInstance : staff) {
            if (staffInstance.getStaffID() == staffID) {
                staffInstance.addActiveNotification(staffInstance.getNotificationFromMessage("Table waiting for order confirmation"));
            }
        }
    }

    /**
     * Removes the notifications from staff who have not accepted the notification.
     */

    public void removeTableNotificationFromStaff() {
        for (StaffInstance staffInstance : staff) {
            staffInstance.removeNotificationFromType(NotificationTypes.ASSIGN);
        }
    }

}
