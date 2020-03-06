package entities;

public class FoodIngredients {
  private int foodID;
  private String ingredients;
  private boolean isAllergen;

  public FoodIngredients(int foodID, String ingredients) {
    this(foodID, ingredients, false);
  }

  public FoodIngredients(int foodID, String ingredients, boolean isAllergen) {
    this.foodID = foodID;
    this.ingredients = ingredients;
    this.isAllergen = isAllergen;
  }

  public boolean isAllergen() {
    return isAllergen;
  }

  public void setAllergen(boolean allergen) {
    isAllergen = allergen;
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
