package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class that stores the ingredients from the database.
 *
 * @author Anas Choudhury, Bhavik Narang
 */
public class Ingredient {
  private int foodID;
  private int ingredientID;
  private String ingredient;
  private boolean allergen;

  /**
   * Constructor that assigns the parameters to the attributes.
   */
  public Ingredient(int foodID, int ingredientID, String ingredient, boolean allergen) {
    this.foodID = foodID;
    this.ingredientID = ingredientID;
    this.ingredient = ingredient;
    this.allergen = allergen;
  }

  /**
   * Constructor for an Ingredient ONLY TO BE USED when adding or editing a food in the database.
   *
   * @param foodID       The ID of the food
   * @param ingredientID The ID of the ingredient referenced as stored in the database
   */
  //TODO Change FoodClass to take different form of Ingredient so this method is not used
  public Ingredient(int foodID, int ingredientID) {
    this.foodID = foodID;
    this.ingredientID = ingredientID;
  }

  /**
   * Returns the ID of the ingredient.
   *
   * @return ID of the ingredient.
   */
  public int getIngredientID() {
    return ingredientID;
  }

  /**
   * Gets the foodID that the ingredient is linked to.
   *
   * @return foodID of the ingredient.
   */
  @JsonIgnore
  public int getFoodID() {
    return foodID;
  }

}
