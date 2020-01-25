package entities;

public class FoodIngredients {
  private int foodID;
  private String ingredients;

  public FoodIngredients(int foodID, String ingredients) {
    this.foodID = foodID;
    this.ingredients = ingredients;
  }

  public int getFoodID() {
    return foodID;
  }

  public void setFoodID(int foodID) {
    this.foodID = foodID;
  }

  public String getIngredients() {
    return ingredients;
  }

  public void setIngredients(String ingredients) {
    this.ingredients = ingredients;
  }
}
