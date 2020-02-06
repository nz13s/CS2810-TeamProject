package entities.serialisers;

import java.util.ArrayList;

public class Ingredient {
    private int ingredientID;
    private String ingredient;
    private boolean allergen;
    private ArrayList<Ingredient> ingredientList;

    public Ingredient(){
        ingredientList = new ArrayList<Ingredient>();
    }

    public int getIngredientID(){
        return ingredientID;
    }

    public String getIngredient(){
        return ingredient;
    }

    public boolean getAllergen(){
        return allergen;
    }

    public void setIngredientID(int ingredientID){
        this.ingredientID = ingredientID;
    }

    public void setIngredient(String ingredient){
        this.ingredient = ingredient;
    }

    public void setAllergen(boolean allergen){
        this.allergen = allergen;
    }

}
