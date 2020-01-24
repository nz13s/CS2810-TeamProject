package entities;
import Food;

public class Orders {
  private int orderID;
  private long timeOrdered;
  private long orderConfirmed;
  private long orderReady;
  private long orderServed;
  private int tableNum;
  private ArrayList<Food> foodItems;

  public Orders(int orderID, long timeOrdered, long orderConfirmed, long orderReady,
                long orderServed, Table table, ArrayList<Food> foodItems) {
    this.orderID = orderID;
    this.timeOrdered = timeOrdered;
    this.orderConfirmed = orderConfirmed;
    this.orderReady = orderReady;
    this.orderServed = orderServed;
    this.tableNum = table.tableNum;
    this.foodItems = foodItems;
  }

  public int getOrderID() {
    return orderID;
  }

  public ArrayList<Food> getFoodItems() {
    return foodItems;
  }

  public void setFoodItems(ArrayList<Food> foodItems) {
    this.foodItems = foodItems;
  }

  public void setOrderID(int orderID) {
    this.orderID = orderID;
  }

  public long getTimeOrdered() {
    return timeOrdered;
  }

  public void setTimeOrdered(long timeOrdered) {
    this.timeOrdered = timeOrdered;
  }

  public long getOrderConfirmed() {
    return orderConfirmed;
  }

  public void setOrderConfirmed(long orderConfirmed) {
    this.orderConfirmed = orderConfirmed;
  }

  public long getOrderReady() {
    return orderReady;
  }

  public void setOrderReady(long orderReady) {
    this.orderReady = orderReady;
  }

  public long getOrderServed() {
    return orderServed;
  }

  public void setOrderServed(long orderServed) {
    this.orderServed = orderServed;
  }

  public int getTableNum() {
    return tableNum;
  }

  public void setTableNum(int tableNum) {
    this.tableNum = tableNum;
  }

  public boolean orderReady() { //this is manually checked in the Kitchen and clicked on the system
    return orderReady != 0;
  }

  public boolean orderServed() { //waiter clicks the Ready button on the system when served
    return orderServed != 0;
  }
}
