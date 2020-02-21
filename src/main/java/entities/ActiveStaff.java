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
}
