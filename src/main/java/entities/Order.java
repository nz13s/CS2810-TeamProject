package entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import databaseInit.Database;
import endpoints.AddToOrder;

import javax.annotation.Nonnull;
import java.sql.SQLException;
import java.util.ArrayList;

public class Order implements IFakeable, ISerialisable {
    protected int orderID;
    protected long timeOrdered = 0;
    protected long orderConfirmed = 0;
    protected long orderPreparing = 0;
    protected long orderReady = 0;
    protected long orderServed = 0;
    protected boolean orderCancelled;
    protected int tableNum;
    private ArrayList<Item> foodItems; //we should make this a Set<Item> as Items are (or, should be) unique

    private transient boolean isFake = true;

    public Order(int orderID, long timeOrdered, long orderConfirmed, long orderPreparing, long orderReady,
                 long orderServed, int tableNum,
                 @Nonnull ArrayList<Item> foodItems) {
        isFake = false; //as we have used the orderID to create this object, it is not fake
        this.orderID = orderID;
        this.timeOrdered = timeOrdered;
        this.orderConfirmed = orderConfirmed;
        this.orderPreparing = orderPreparing;
        this.orderReady = orderReady;
        this.orderServed = orderServed;
        this.orderCancelled = false;
        this.tableNum = tableNum;
        this.foodItems = foodItems;
    }

    public Order(int orderID, long timeOrdered, long orderConfirmed, long orderReady,
                 long orderServed, int tableNum) {
        new Order(orderID, timeOrdered, orderConfirmed, orderPreparing, orderReady, orderServed, tableNum, new ArrayList<>());
        isFake = false;
        orderCancelled = false;
    }

    public Order(long timeOrdered, int tableNum, ArrayList<Item> foodItems) {
        this.timeOrdered = timeOrdered;
        this.tableNum = tableNum;
        this.foodItems = foodItems;
    }

    public Order(long timeOrdered, int tableNum) {
        this(timeOrdered, tableNum, new ArrayList<>());
    }

    /**
     * Constructor for Order, called by the {@link AddToOrder} class when creating a new order
     */
    public Order() {
        this.foodItems = new ArrayList<>();
    }

    public int getOrderID() {
        return orderID;
    }

    @Nonnull
    public ArrayList<Item> getFoodItems() {
        return foodItems;
    }

    public void addFoodItem(@Nonnull Item item) {
        this.foodItems.add(item);
    }

    /**
     * Method to add {@link Food} items to the Order with amounts, deals with duplicates
     *
     * @param ID     The ID of the Food to add, as stored in the database
     * @param amount The amount of the Food to add to the Order
     */
    public void addFoodItem(int ID, int amount) throws SQLException {
        try {
            Food food = Database.FOODS.getFoodByID(ID, false);
            if (alreadyInOrder(food)) {
                Item item = getItem(food);
                if (item == null) {
                    return;
                }
                item.add(amount);
            } else {
                foodItems.add(new Item(food, amount));
            }
        } catch (SQLException e) {
            throw new SQLException();
        }
    }

    /**
     * Method to remove {@link Food} Items from the Order with amounts, removes them from the ArrayList if their amount is 0 or less
     *
     * @param ID     The ID of the Food to remove, as stored in the database
     * @param amount The amount of the Food to remove from the Order
     */
    public void removeFoodItem(int ID, int amount) {
        try {
            Food food = Database.FOODS.getFoodByID(ID, false);
            if (alreadyInOrder(food)) {
                Item item = getItem(food);
                if (item == null) {
                    return;
                }
                item.remove(amount);
                if (item.getAmount() <= 0) {
                    foodItems.remove(item);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks to see whether the inputted {@link Food} is already in the current Order
     *
     * @param food The Food being checked against
     * @return Whether the Order already contains the given food
     */
    private boolean alreadyInOrder(Food food) {
        Item tempItem = new Item(food, 0);
        return foodItems.contains(tempItem);
    }

    /**
     * Gets the inputted {@link Food} item if it is in the Order, null otherwise
     *
     * @param food The Food to locate in the Order
     * @return The {@link Item} in the order with the Food, null if it does not exist
     */
    private Item getItem(Food food) {
        for (Item item : foodItems) {
            if (item.getFood().getFoodID() == food.getFoodID()) {
                return item;
            }
        }
        return null;
    }

    /**
     * Use a constructor with the items instead.
     *
     * @param foodItems the new item List
     */
    @Deprecated
    public void setFoodItems(@Nonnull ArrayList<Item> foodItems) {
        this.foodItems = foodItems;
    }

    public void setOrderID(int orderID) {
        this.orderID = orderID;
    }

    public long getTimeOrdered() {
        return timeOrdered;
    }

    public void setTimeOrdered(long timeOrdered) {
        this.timeOrdered = timeOrdered;
    }

    public long getOrderConfirmed() {
        return orderConfirmed;
    }

    public void setOrderConfirmed(long orderConfirmed) {
        this.orderConfirmed = orderConfirmed;
    }

    public long getOrderPreparing() {
        return orderPreparing;
    }

    public void setOrderPreparing(long orderPreparing) {
        this.orderPreparing = orderPreparing;
    }

    public long getOrderReady() {
        return orderReady;
    }

    public void setOrderReady(long orderReady) {
        this.orderReady = orderReady;
    }

    public long getOrderServed() {
        return orderServed;
    }

    public void setOrderServed(long orderServed) {
        this.orderServed = orderServed;
    }

    public int getTableNum() {
        return tableNum;
    }

    public void setTableNum(int tableNum) {
        this.tableNum = tableNum;
    }

    public boolean orderReady() { //this is manually checked in the Kitchen and clicked on the system
        return orderReady != 0;
    }

    public boolean orderServed() { //waiter clicks the Ready button on the system when served
        return orderServed != 0;
    }

    @Override
    @JsonIgnore
    public boolean isFake() {
        return isFake;
    }
}
