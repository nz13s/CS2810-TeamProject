package entities;

import javax.annotation.Nonnull;

public class Food {
  private int foodID;
  private String foodName;
  private String foodDescription;
  private int calories;
  private long price;
  private boolean available;
  private int categoryID;

  public Food(int foodID, @Nonnull String foodName, @Nonnull String foodDescription, int calories, long price,
              boolean available, int categoryID) {
    this.foodID = foodID;
    this.foodName = foodName;
    this.foodDescription = foodDescription;
    this.calories = calories;
    this.price = price;
    this.available = available;
    this.categoryID = categoryID;
  }

  public int getFoodID() {
    return foodID;
  }

  public void setFoodID(int foodID) {
    this.foodID = foodID;
  }

  @Nonnull
  public String getFoodName() {
    return foodName;
  }

  public void setFoodName(String foodName) {
    this.foodName = foodName;
  }

  @Nonnull
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

  public long getPrice() {
    return price;
  }

  public void setPrice(long price) {
    this.price = price;
  }

  public boolean isAvailable() {
    return available;
  }

  public void setAvailable(boolean available) {
    this.available = available;
  }

  public int getCategoryID() { return categoryID; };

  public void setCategoryID(int categoryID) { this.categoryID = categoryID; }

  @Override
  public String toString() {
    //todo fix this to use a Jackson serialiser
    return "Food{" +
        "foodName='" + foodName + '\'' +
        '}';
  }
}
