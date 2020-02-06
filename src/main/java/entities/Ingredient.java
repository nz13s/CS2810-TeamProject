package entities;

/**
 * Class that stores the ingredients from the database.
 *
 * @author Anas
 */

public class Ingredient {

    private int ingredientID;
    private String ingredient;
    private boolean allergen;

    /**
     * Constructor that assigns the allergen to false by default.
     */

    public Ingredient() {
        allergen = false;
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

}
