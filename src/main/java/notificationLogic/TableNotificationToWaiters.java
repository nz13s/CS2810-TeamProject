package notificationLogic;

import entities.ActiveStaff;
import entities.Notification;
import entities.StaffInstance;
import entities.Table;

import java.util.List;

public class TableNotificationToWaiters {

    private List<StaffInstance> staff;

    public TableNotificationToWaiters() {
        staff = new ActiveStaff().getAllActiveStaff();
    }

    public void addTableNotificationToAllStaff() {

        //temp table object - will be changed to actual table object depending on how tables are going to see assigned.
        Table table = new Table(1,1,false,null);

        for (StaffInstance staffInstance : staff) {
            staffInstance.addNotification(new Notification(table, "Table waiting for order confirmation"));
        }
    }

}
