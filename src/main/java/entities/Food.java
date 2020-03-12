package entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.ArrayList;

public class Food implements ISerialisable, IFakeable {
  private int foodID = -1;
  private String foodName;
  private String foodDescription;
  private int calories;
  private BigDecimal price;
  private boolean available;
  private int categoryID;
  private ArrayList<Ingredient> ingredients;
  private String imageURL;

  public Food(int foodID, @Nonnull String foodName, @Nonnull String foodDescription, int calories, BigDecimal price,
              boolean available, int categoryID, ArrayList<Ingredient> ingredients, @Nullable String imageURL) {
    this.foodID = foodID;
    this.foodName = foodName;
    this.foodDescription = foodDescription;
    this.calories = calories;
    this.price = price;
    this.available = available;
    this.categoryID = categoryID;
    this.ingredients = ingredients;
    this.imageURL = imageURL;
  }

  public Food(@Nonnull String foodName, @Nonnull String foodDescription, int calories, BigDecimal price,
              boolean available, int categoryID, ArrayList<Ingredient> ingredients, @Nullable String imageURL) {
    this.foodName = foodName;
    this.foodDescription = foodDescription;
    this.calories = calories;
    this.price = price;
    this.available = available;
    this.categoryID = categoryID;
    this.ingredients = ingredients;
    this.imageURL = imageURL;
  }

  public String getImageURL() {
    return imageURL;
  }

  public void setImageURL(String imageURL) {
    this.imageURL = imageURL;
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

  @Override
  public boolean isFake() {
    return foodID < 0;
  }
}
