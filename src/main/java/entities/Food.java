package entities;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Food {
  private int foodID;
  private String foodName;
  private String foodDescription;
  private int calories;
  private BigDecimal price;
  private boolean available;
  private int categoryID;
  private ArrayList<Ingredient> ingredients;

  public Food(int foodID, @Nonnull String foodName, @Nonnull String foodDescription, int calories, BigDecimal price,
              boolean available, int categoryID, ArrayList<Ingredient> ingredients) {
    this.foodID = foodID;
    this.foodName = foodName;
    this.foodDescription = foodDescription;
    this.calories = calories;
    this.price = price;
    this.available = available;
    this.categoryID = categoryID;
    this.ingredients = ingredients;
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

  public int getCategoryID() { return categoryID; };

  public void setCategoryID(int categoryID) { this.categoryID = categoryID; }

  public ArrayList<Ingredient> getIngredients() {
    return ingredients;
  }

  public int size() {
    return ingredients.size();
  }

  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
  }

}
