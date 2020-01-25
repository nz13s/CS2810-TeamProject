import java.util.ArrayList;

/**
 * Holds the tables order as they are ordering it.
 */
public class Basket {

    ArrayList<Item> order;//An ArrayList to store all the current items in the order

    /**
     * Constructor for the basket, creates a new ArrayList to store all the {@link Item} in
     */
    public Basket() {
        order = new ArrayList<>();
    }

    /**
     * Gets the Item from the database/local cache and adds it to the basket
     *
     * @param ID     The ID of the order as stored in the database
     * @param number The number of that item to add
     */
    public void addToBasket(int ID, int number) {
        Item item;
        if ((item = get(ID)) != null) {
            item.add(number);
        } else {
            //TODO @Oliver vvvvv this bit please
            //Database.getItem(ID);
            //if(item.exists()){
            Item newItem = new Item(ID, "Something", number);
            order.add(newItem);
            //}
        }
    }

    /**
     * Removes the specified amount from the order
     *
     * @param ID     The ID of the item to remove as stored in the database
     * @param number The number of the item to remove
     */
    public void removeFromBasket(int ID, int number) {
        Item item;
        if ((item = get(ID)) != null) {
            boolean remove = item.remove(number);
            if (remove) {
                order.remove(item);
            }
        }
    }

    /**
     * Checks whether the given {@link Item} is within the order already
     *
     * @param ID The ID of the item to find as is in the database
     * @return Whether the order already contains the specified Item
     */
    private boolean contains(int ID) {
        for (Item item : order) {
            if (item.getID() == ID) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the specified {@link Item} or null if it is not there
     *
     * @param ID The ID of the item to find as stored in the database
     * @return The Item found, null otherwise
     */
    private Item get(int ID) {
        for (Item item : order) {
            if (item.getID() == ID) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the ArrayList with all the {@link Item} in
     *
     * @return The ArrayList of Items in the Basket
     */
    public ArrayList<Item> getBasket() {
        return this.order;
    }
}
