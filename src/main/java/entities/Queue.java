package entities;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that stores all Order objects inside an ArrayList.
 *
 * @author Jatin Khatra
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
   * Returns the list of all Orders in the queue.
   *
   * @return list of Orders.
   */
  @Nonnull
  public List<? extends Order> getList() {
    return queue;
  }

}
