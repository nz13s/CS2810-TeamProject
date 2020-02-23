package entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class Table {
  public int tableNum;
  private int seatsAvailable;
  private boolean occupied;
  private List<Order> orders; //orders from this specific entities.Table
  private StaffInstance waiter;

  public Table(int tableNum, int seatsAvailable, boolean occupied, @Nullable List<Order> orders) {
    if (orders == null) orders = new ArrayList<>();
    this.tableNum = tableNum;
    this.seatsAvailable = seatsAvailable;
    this.occupied = occupied;
    this.orders = orders;
  }

  public int getTableNum() {
    return tableNum;
  }

  public void setTableNum(int tableNum) {
    this.tableNum = tableNum;
  }

  public int getSeatsAvailable() {
    return seatsAvailable;
  }

  public void setSeatsAvailable(int seatsAvailable) {
    this.seatsAvailable = seatsAvailable;
  }

  public boolean isOccupied() {
    return occupied;
  }

  public void setOccupied(boolean occupied) {
    this.occupied = occupied;
  }

  @Nonnull
  public List<Order> getOrders() {
    return orders;
  }

  public void addOrder(Order order) { orders.add(order); }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  public void newGuest() {
    setSeatsAvailable(seatsAvailable--);
  }

  public void guestLeave() {
    setSeatsAvailable(seatsAvailable++);
  }

  public StaffInstance getWaiter() {
    return waiter;
  }

  public void setWaiter(StaffInstance waiter) {
    this.waiter = waiter;
  }
}
