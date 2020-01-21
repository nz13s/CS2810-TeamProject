import java.math.BigInteger;

public class Orders {
  private int orderID;
  private BigInteger timeOrdered;
  private BigInteger orderConfirmed;
  private BigInteger orderReady;
  private BigInteger orderServed;
  private int tableNum;

  public Orders(int orderID, BigInteger timeOrdered, BigInteger orderConfirmed, BigInteger orderReady,
                BigInteger orderServed, Table table) {
    this.orderID = orderID;
    this.timeOrdered = timeOrdered;
    this.orderConfirmed = orderConfirmed;
    this.orderReady = orderReady;
    this.orderServed = orderServed;
    this.tableNum = table.tableNum;
  }

  public int getOrderID() {
    return orderID;
  }

  public void setOrderID(int orderID) {
    this.orderID = orderID;
  }

  public BigInteger getTimeOrdered() {
    return timeOrdered;
  }

  public void setTimeOrdered(BigInteger timeOrdered) {
    this.timeOrdered = timeOrdered;
  }

  public BigInteger getOrderConfirmed() {
    return orderConfirmed;
  }

  public void setOrderConfirmed(BigInteger orderConfirmed) {
    this.orderConfirmed = orderConfirmed;
  }

  public BigInteger getOrderReady() {
    return orderReady;
  }

  public void setOrderReady(BigInteger orderReady) {
    this.orderReady = orderReady;
  }

  public BigInteger getOrderServed() {
    return orderServed;
  }

  public void setOrderServed(BigInteger orderServed) {
    this.orderServed = orderServed;
  }

  public int getTableNum() {
    return tableNum;
  }

  public void setTableNum(int tableNum) {
    this.tableNum = tableNum;
  }

  public boolean orderReady() { //this is manually checked in the Kitchen and clicked on the system
    return !orderReady.equals(BigInteger.ZERO);
  }

  public boolean orderServed() { //waiter clicks the Ready button on the system when served
    return !orderServed.equals(BigInteger.ZERO);
  }
}
