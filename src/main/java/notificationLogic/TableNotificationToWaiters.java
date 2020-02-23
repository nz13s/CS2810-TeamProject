package notificationLogic;

import entities.*;

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
     */

    public boolean addTableToStaff(Table table) {
        for (StaffInstance staffInstance : staff) {
            if (staffInstance.getTables().size() < 3 && !staffInstance.hasTable(table)) {
                staffInstance.addTable(table);
                staffInstance.addNotification(new Notification(table, NotificationTypes.ASSIGN));
                table.setWaiter(staffInstance);
                return true;
            }
        }
        return false;
    }
}
