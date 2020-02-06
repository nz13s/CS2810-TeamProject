package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Class that stores the ingredients from the database.
 *
 * @author Anas
 * @author Bhavik
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
     * Returns the ID of the ingredient.
     *
     * @return ID of the ingredient.
     */
    public int getIngredientID(){
        return ingredientID;
    }

    /**
     * Returns the ingredient name.
     *
     * @return ingredient name.
     */

    public String getIngredient(){
        return ingredient;
    }

    /**
     * Outputs whether the ingredient is an allergen or not.
     *
     * @return ingredient's allergen state.
     */

    public boolean getAllergen(){
        return allergen;
    }

    /**
     * Sets the ID of the ingredient.
     *
     * @param ingredientID ID of the ingredient.
     */

    public void setIngredientID(int ingredientID){
        this.ingredientID = ingredientID;
    }

    /**
     * Sets the name of the ingredient
     *
     * @param ingredient name of the ingredient.
     */

    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }

    /**
     * Setter to identify if the ingredient is a allergen or not.
     *
     * @param allergen boolean value indicating if an ingredient is an allergen or not.
     */

    public void setAllergen(boolean allergen){
        this.allergen = allergen;
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

    /**
     * Sets the foodID of the ingredient.
     *
     * @param foodID foodID of the ingredient.
     */

    public void setFoodID(int foodID) {
        this.foodID = foodID;
    }

}
