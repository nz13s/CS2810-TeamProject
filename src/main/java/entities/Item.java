package entities;

/**
 * An item stored in {@link Basket}
 */

//TODODONE hold a entities.Food object rather than ID+name @cameron
public class Item {

    private Food food;
    private int amount;

    /**
     * Constructor for an Item to go in the {@link Basket}
     *
     * @param food   The food item in the order
     * @param amount The number of the item in the order
     */
    public Item(Food food, int amount) {
        this.food = food;
        this.amount = amount;
    }

    /**
     * Removes one from the number of the specified item in the order
     *
     * @return Whether the amount is 0 or less, so could be removed
     */
    public boolean remove() {
        return remove(1);
    }

    /**
     * Removes the inputted amount to the number of the specified item in the order
     *
     * @param amount The amount to remove from the order
     * @return Whether the amount is 0 or less, so could be removed
     */
    public boolean remove(int amount) {
        this.amount -= amount;
        return this.amount <= 0;
    }

    /**
     * Adds one to the number of the selected item in the order
     */
    public void add() {
        this.amount++;
    }

    /**
     * Adds the inputted amount to the number in the order
     *
     * @param amount The amount to add to the order amount
     */
    public void add(int amount) {
        this.amount += amount;
    }

    /**
     * The Food item ordered by the suer
     *
     * @return The Food item ordered by the user
     */
    public Food getFood() {
        return this.food;
    }

    /**
     * Gets the amount of this item in the order
     *
     * @return The amount of the item in the current order
     */
    public int getAmount() {
        return this.amount;
    }

    /**
     * Checks whether two Items are the same, compares on {@link Food} ID's as stored in the database
     * @param o The object being compared to the Item
     * @return Whether the two Items are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if(!(o instanceof Item)){
            return false;
        }
        return this.food.getFoodID() == ((Item) o).getFood().getFoodID();
    }
}
