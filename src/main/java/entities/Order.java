package entities;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class Order implements IFakeable{
  private int orderID;
  private long timeOrdered = 0;
  private long orderConfirmed = 0;
  private long orderReady = 0;
  private long orderServed = 0;
  private int tableNum;
  private ArrayList<Item> foodItems; //we should make this a Set<Item> as Items are (or, should be) unique

  private boolean isFake = true;

  public Order(int orderID, long timeOrdered, long orderConfirmed, long orderReady,
                long orderServed, int tableNum,@Nonnull ArrayList<Item> foodItems) {
    isFake = false; //as we have used the orderID to create this object, it is not fake
    this.orderID = orderID;
    this.timeOrdered = timeOrdered;
    this.orderConfirmed = orderConfirmed;
    this.orderReady = orderReady;
    this.orderServed = orderServed;
    this.tableNum = tableNum;
    this.foodItems = foodItems;
  }

  public Order(int orderID, long timeOrdered, long orderConfirmed, long orderReady,
               long orderServed, int tableNum) {
    new Order(orderID, timeOrdered, orderConfirmed, orderReady, orderServed, tableNum, new ArrayList<>());
    isFake = false;
  }

  public Order(long timeOrdered, int tableNum, ArrayList<Item> foodItems){
    this.timeOrdered = timeOrdered;
    this.tableNum = tableNum;
    this.foodItems = foodItems;
  }

  public Order(long timeOrdered, int tableNum){
    this(timeOrdered, tableNum, new ArrayList<>());
  }

  public int getOrderID() {
    return orderID;
  }

  @Nonnull
  public ArrayList<Item> getFoodItems() {
    return foodItems;
  }

  public void addFoodItem(@Nonnull Item item){
    this.foodItems.add(item);
  }

  /**
   * Use a constructor with the items instead.
   * @param foodItems the new item List
   */
  @Deprecated
  public void setFoodItems(@Nonnull ArrayList<Item> foodItems) {
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

  @Override
  public boolean isFake() {
    return isFake;
  }
}
