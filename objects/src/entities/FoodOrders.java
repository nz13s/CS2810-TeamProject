package entities;

public class FoodOrders {
  private int orderID;
  private int foodID;

  public FoodOrders(int orderID, int foodID) {
    this.orderID = orderID;
    this.foodID = foodID;
  }

  public int getOrderID() {
    return orderID;
  }

  public void setOrderID(int orderID) {
    this.orderID = orderID;
  }

  public int getFoodID() {
    return foodID;
  }

  public void setFoodID(int foodID) {
    this.foodID = foodID;
  }
}
