package entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores a list of active {@link StaffInstance} so that they can be referenced to be notified of events
 */
public class ActiveStaff {

    private List<StaffInstance> staff;

    public ActiveStaff() {
        staff = new ArrayList<>();
    }

    /**
     * Adds the inputted notification to the specified staff member by their staffID
     *
     * @param staffID The staffID, as stored in the database, to send the notification to
     * @param message The notification to pass to the specified staff member
     */
    public void addNotification(int staffID, String message) {
        StaffInstance employee = getStaffByID(staffID);
        if (employee != null) {
            addNotification(employee, message);
        }
        //TODO Some sort of error message if the staff member is not active
    }

    /**
     * Adds the inputted notification to the specified staff member
     * @param employee The staff member to send the notification to
     * @param message The notification to pass to the specified staff member
     */
    public void addNotification(StaffInstance employee, String message) {
        employee.addNotification(message);
    }

    /**
     * Adds a new staff member to the list of active staff members
     *
     * @param employee The staff member to add to the list of active staff
     */
    public void addStaff(StaffInstance employee) {
        staff.add(employee);
    }

    /**
     * Adds a new staff member to the list of active staff members
     *
     * @param staffID The String ID, as stored in the database, of the staff member to be added to the list of
     *                active staff
     */
    public void addStaff(int staffID) {
        //TODO Add validation for a valid staff member being added
        this.addStaff(new StaffInstance(staffID));
    }

    /**
     * Removes the specified staff member from the active list of staff
     *
     * @param staffID The ID, as stored in the database, of the staff member to be removed from the list of
     *                active staff
     */
    public void remove(int staffID) {
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
    public void remove(StaffInstance employee) {
        if (staff.contains(employee)) {
            removeStaffFromActive(employee);
        }
    }

    /**
     * Removes the specified staff member from the list of active staff members, reallocates uncompleted tasks
     *
     * @param employee The staff member to remove from the list of active staff
     */
    private void removeStaffFromActive(StaffInstance employee) {
        //TODO Notification reallocation
        staff.remove(employee);
    }

    /**
     * Gets the specified staff member from the list of active staff by their staffID as is stored in the database,
     * returns null if the staff member does not exist or is not currently active
     *
     * @param staffID The ID, as stored in the database, of the staff member to be located
     * @return The {@link StaffInstance} of the staff member with the specified staffID, null if not there/active
     */
    private StaffInstance getStaffByID(int staffID) {
        for (StaffInstance employee : staff) {
            if (employee.getStaffID() == staffID) {
                return employee;
            }
        }
        return null;
    }
}
