package entities;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * Class that stores all Order objects inside an ArrayList.
 */

public class Queue {

    private ArrayList<Order> queue;

    /**
     * Initialises the ArrayList queue.
     */

    public Queue() {
        queue = new ArrayList<Order>();
    }

    /**
     * Initialises the ArrayList queue through an ArrayList parameter.
     *
     * @param queue ArrayList containing Orders.
     */

    public Queue(ArrayList<Order> queue) {
        this.queue = queue;
    }

    /**
     * Gets the size of the Queue.
     *
     * @return size of the queue.
     */

    public int size() {
        return queue.size();
    }

    /**
     * Gets the Order element at the index given.
     *
     * @param i index of Order object.
     * @return Order object at index i.
     */

    public Order getOrder(int i) {
        return queue.get(i);
    }

    /**
     * Adds an order to the Queue.
     *
     * @param order Order to be added to the queue.
     */

    public void addOrder(Order order) {
        queue.add(order);
    }

    /**
     * Returns the list of all Orders in the queue.
     *
     * @return list of Orders.
     */

    @Nonnull
    public ArrayList<Order> getList() {
        return queue;
    }

}
