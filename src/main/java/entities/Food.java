package entities;

import java.math.BigDecimal;
import java.util.ArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Class implementing a Food item.
 *
 * @author Nick Bogachev, Oliver Graham, Jatin Khatra
 */
public class Food implements ISerialisable, IFakeable {
  private boolean available;
  private int foodID = -1;
  private String foodName;
  private String foodDescription;
  private int calories;
  private BigDecimal price;
  private int categoryID;
  private ArrayList<Ingredient> ingredients;
  private String imageUrl;


  /**
   * Standard Food constructor.
   *
   * @param foodID
   * @param foodName
   * @param foodDescription
   * @param calories
   * @param price
   * @param categoryID
   * @param ingredients
   * @param imageUrl
   */
  public Food(int foodID, @Nonnull String foodName, @Nonnull String foodDescription, int calories,
              BigDecimal price, int categoryID, ArrayList<Ingredient> ingredients,
              @Nullable String imageUrl) {
    this.foodID = foodID;
    this.foodName = foodName;
    this.foodDescription = foodDescription;
    this.calories = calories;
    this.price = price;
    this.categoryID = categoryID;
    this.ingredients = ingredients;
    this.imageUrl = imageUrl;
  }

  /**
   * Food constructor without assigning food ID.
   *
   * @param foodName
   * @param foodDescription
   * @param calories
   * @param price
   * @param categoryID
   * @param ingredients
   * @param imageUrl
   */
  public Food(@Nonnull String foodName, @Nonnull String foodDescription, int calories,
              BigDecimal price, int categoryID, ArrayList<Ingredient> ingredients,
              @Nullable String imageUrl) {
    this.foodName = foodName;
    this.foodDescription = foodDescription;
    this.calories = calories;
    this.price = price;
    this.categoryID = categoryID;
    this.ingredients = ingredients;
    this.imageUrl = imageUrl;
  }

  /**
   * Food constructor with an available boolean field.
   *
   * @param food_id
   * @param food_name
   * @param food_description
   * @param calories
   * @param price
   * @param available
   * @param category_id
   * @param ingredients
   * @param image_url
   */
  public Food(int food_id, @Nonnull String food_name, @Nonnull String food_description,
              int calories, BigDecimal price, boolean available, int category_id,
              ArrayList<Ingredient> ingredients, @Nonnull String image_url) {
    this.foodID = food_id;
    this.foodName = food_name;
    this.foodDescription = food_description;
    this.calories = calories;
    this.price = price;
    this.available = available;
    this.categoryID = category_id;
    this.ingredients = ingredients;
    this.imageUrl = image_url;
  }

  public String getImageUrl() {
    return imageUrl;
  }

  public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
  }

  public int getFoodID() {
    return foodID;
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

  public int getCategoryID() {
    return categoryID;
  }

  public void setCategoryID(int categoryID) {
    this.categoryID = categoryID;
  }

  public ArrayList<Ingredient> getIngredients() {
    return ingredients;
  }

  public void addIngredient(Ingredient ingredient) {
    ingredients.add(ingredient);
  }

  @Override
  public boolean isFake() {
    return foodID < 0;
  }
}
