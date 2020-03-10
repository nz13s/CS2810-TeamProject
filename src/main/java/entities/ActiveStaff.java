package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a list of active {@link StaffInstance} so that they can be referenced to be notified of events
 */
public class ActiveStaff {

    private static List<StaffInstance> staff = new ArrayList<>();

    /**
     * Adds the inputted notification to the specified staff member by their staffID
     *
     * @param staffID The staffID, as stored in the database, to send the notification to
     * @param message The notification to pass to the specified staff member
     */
    public static void addNotification(int staffID, Notification message) {
        StaffInstance employee = getStaffByID(staffID);
        if (employee != null) {
            addNotification(employee, message);
        }
        //TODO Some sort of error message if the staff member is not active
    }

    /**
     * Adds the inputted notification to the specified staff member
     *
     * @param employee The staff member to send the notification to
     * @param message  The notification to pass to the specified staff member
     */
    public static void addNotification(StaffInstance employee, Notification message) {
        employee.addNotification(message);
    }

    public static StaffInstance[] findStaffForTable(int tableNum) {
        return (StaffInstance[]) staff.stream().filter(member -> member.getTable(tableNum) != null).toArray();
    }

    /**
     * Adds a new staff member to the list of active staff members
     *
     * @param employee The staff member to add to the list of active staff
     */
    public static void addStaff(StaffInstance employee) {
        staff.add(employee);
    }

    /**
     * Adds a new staff member to the list of active staff members
     *
     * @param staffID The int ID, as stored in the database, of the staff member to be added to the list of
     *                active staff
     */
    public static void addStaff(int staffID) {
        //TODO Add validation for a valid staff member being added
        addStaff(new StaffInstance(staffID));
    }

    /**
     * Removes the specified staff member from the active list of staff
     *
     * @param staffID The int ID, as stored in the database, of the staff member to be removed from the list of
     *                active staff
     */
    public static void remove(int staffID) {
        StaffInstance employee = getStaffByID(staffID);
        if (employee != null) {
            removeStaffFromActive(employee);
        }
    }

    /**
     * Removes the specified staff member from the active list of staff
     *
     * @param employee The staff member to remove from active
     */
    public static void remove(StaffInstance employee) {
        if (staff.contains(employee)) {
            removeStaffFromActive(employee);
        }
    }

    /**
     * Removes the specified staff member from the list of active staff members, reallocates uncompleted tasks
     *
     * @param employee The staff member to remove from the list of active staff
     */
    private static void removeStaffFromActive(StaffInstance employee) {
        //TODO Notification reallocation
        staff.remove(employee);
    }

    /**
     * Gets the specified staff member from the list of active staff by their staffID as is stored in the database,
     * returns null if the staff member does not exist or is not currently active
     *
     * @param staffID The int ID, as stored in the database, of the staff member to be located
     * @return The {@link StaffInstance} of the staff member with the specified staffID, null if not there/active
     */
    public static StaffInstance getStaffByID(int staffID) {
        for (StaffInstance employee : staff) {
            if (employee.getStaffID() == staffID) {
                return employee;
            }
        }
        return null;
    }

    /**
     * Method that returns all active staff to send notifications to.
     *
     * @return List of StaffInstance objects.
     */

    public static List<StaffInstance> getAllActiveStaff() {
        return staff;
    }

    /**
     * Method returns whether the staff member specified is in the list of active staff
     *
     * @param staffID The int ID of the staff member as stored in the database
     * @return True if the specified staff member is in the list of active staff, false otherwise
     */
    public static boolean isActive(int staffID) {
        for (StaffInstance employee : staff) {
            if (employee.getStaffID() == staffID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Method Adds the table to a Random staff from the currently Active Staff,
     * Whose table list is < 3. Sets table to occupied and removes from NeedWaiter List.
     *
     * @param table The table that needs a waiter
     * @return true if successful
     */
    public static boolean addTableToRandomStaff(Table table) {
        for (StaffInstance staffInstance : staff) {
            if (staffInstance.getTables().size() < 3
                    && !staffInstance.hasTable(table)
                    && table.getWaiter() == null) {

                staffInstance.addTable(table);
                staffInstance.addNotification(new Notification(table, NotificationTypes.ASSIGN));
                table.setWaiter(staffInstance);
                table.setOccupied(true);
                TableState.removeNeedWaiter(table);
                return true;
            }
        }
        return false;
    }

    /**
     * Method Adds the table to a Specified staff from the currently Active Staff,
     * Sets table to occupied and removes from NeedWaiter List.
     *
     * @param table The table that needs a waiter
     * @return true if successful
     */
    public static boolean addTableToStaff(Table table, StaffInstance staff) {
        if (!staff.hasTable(table)
                && table.getWaiter() == null) {
            staff.addTable(table);
            staff.addNotification(new Notification(table, NotificationTypes.ASSIGN));
            table.setWaiter(staff);
            table.setOccupied(true);
            TableState.removeNeedWaiter(table);
            return true;
        }
        return false;
    }

    /**
     * Method sends a notification to all Active staff.
     *
     * @param notif The notification
     * @return false if no ActiveStaff
     */
    public static boolean notifyAll(Notification notif) {
        if (staff.size() > 0) {
            for (StaffInstance staffInstance : staff) {
                addNotification(staffInstance, notif);
            }
            return true;
        }
        return false;
    }

    /**
     * Method finds a Waiter that is assigned to a table from Active staff.
     *
     * @param t The table
     * @return null if no Waiter is assigned this table.
     */
    public static StaffInstance findTableWaiter(Table t) {
        for (StaffInstance staffInstance : staff) {
            if (staffInstance.getTables().contains(t)) {
                return staffInstance;
            }
        }
        return null;
    }

}
