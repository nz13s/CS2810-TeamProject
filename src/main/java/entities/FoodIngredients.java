package entities;

/**
 * A class for food ingredients for a food item.
 *
 * @author Oliver Graham
 */
public class FoodIngredients implements ISerialisable {
  private int foodID;
  private String ingredients;
  private boolean isAllergen;

  public FoodIngredients(int foodID, String ingredients, boolean isAllergen) {
    this.foodID = foodID;
    this.ingredients = ingredients;
    this.isAllergen = isAllergen;
  }

}
