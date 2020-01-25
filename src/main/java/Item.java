/**
 * An item stored in {@link Basket}
 */
public class Item{

    private int ID;
    private String name;
    private int amount;

    /**
     * Constructor for an Item to go in the {@link Basket}
     * @param ID The ID of the item as is in the database
     * @param name The string name of the item for the user
     * @param amount The number of the item in the order
     */
    public Item(int ID, String name, int amount){
        this.ID = ID;
        this.name = name;
        this.amount =amount;
    }

    /**
     * Removes one from the number of the specified item in the order
     * @return Whether the amount is 0 or less, so could be removed
     */
    public boolean remove(){
        this.amount--;
        return this.amount <= 0;
    }

    /**
     * Removes the inputted amount to the number of the specified item in the order
     * @param amount The amount to remove from the order
     * @return Whether the amount is 0 or less, so could be removed
     */
    public boolean remove(int amount){
        this.amount -= amount;
        return this.amount <= 0;
    }

    /**
     * Adds one to the number of the selected item in the order
     */
    public void add(){
        this.amount++;
    }

    /**
     * Adds the inputted amount to the number in the order
     * @param amount The amount to add to the order amount
     */
    public void add(int amount){
        this.amount += amount;
    }

    /**
     * The ID of the item
     * @return The ID of the item as it is stored in the database
     */
    public int getID(){
        return this.ID;
    }

    /**
     * The Name of the item
     * @return The String mane of the item for the user
     */
    public String getName(){
        return this.name;
    }

    /**
     * Gets the amount of this item in the order
     * @return The amount of the item in the current order
     */
    public int getAmount(){
        return this.amount;
    }
}
