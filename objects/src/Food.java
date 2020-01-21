import java.math.BigDecimal;

public class Food {
  private int foodID;
  private String foodName;
  private String foodDescription;
  private int calories;
  private BigDecimal price;
  private boolean available;

  public Food(int foodID, String foodName, String foodDescription, int calories, BigDecimal price,
              boolean available) {
    this.foodID = foodID;
    this.foodName = foodName;
    this.foodDescription = foodDescription;
    this.calories = calories;
    this.price = price;
    this.available = available;
  }

  public int getFoodID() {
    return foodID;
  }

  public void setFoodID(int foodID) {
    this.foodID = foodID;
  }

  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  public String getFoodDescription() {
    return foodDescription;
  }

  public void setFoodDescription(String foodDescription) {
    this.foodDescription = foodDescription;
  }

  public int getCalories() {
    return calories;
  }

  public void setCalories(int calories) {
    this.calories = calories;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public void setPrice(BigDecimal price) {
    this.price = price;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }
}
