package entities;

import java.util.List;

public class Table {
  public int tableNum;
  private int seatsAvailable;
  private boolean occupied;
  private List<Order> orders; //orders from this specific entities.Table

  public Table(int tableNum, int seatsAvailable, List<Order> orders) {
    this.tableNum = tableNum;
    this.seatsAvailable = seatsAvailable;
    this.occupied = false;
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

  public List<Order> getOrders() {
    return orders;
  }

  public void setOrders(List<Order> orders) {
    this.orders = orders;
  }

  public void newGuest() {
    setSeatsAvailable(seatsAvailable--);
  }

  public void guestLeave() {
    setSeatsAvailable(seatsAvailable++);
  }


}
