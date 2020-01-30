package entities;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores all Order objects inside an ArrayList.
 */

public class Queue {

    private List<? extends Order> queue;

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

    public Queue(List<? extends Order> queue) {
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
     * Returns the list of all Orders in the queue.
     *
     * @return list of Orders.
     */

    @Nonnull
    public List<? extends Order> getList() {
        return queue;
    }

}
