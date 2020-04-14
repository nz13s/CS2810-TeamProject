package entities;

/**
 * A class to initialise Food Orders on frontend as an object to store in the database.
 *
 * @author Nick Bogachev, Jatin Khatra
 */
public class FoodOrders implements ISerialisable {
  private int orderID;
  private int foodID;
  private int quantity;

  public FoodOrders(int orderID, int foodID, int quantity) {
    this.orderID = orderID;
    this.foodID = foodID;
    this.quantity = quantity;
  }

}
