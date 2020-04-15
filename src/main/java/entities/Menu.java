package entities;

import java.util.ArrayList;
import javax.annotation.Nonnull;

/**
 * Class that stores all categories of the food menu.
 *
 * @author Jatin Khatra, Nick Bogachev, Oliver Graham
 */
public class Menu implements ISerialisable {

  private ArrayList<Category> menu;

  /**
   * Constructor initialises the menu ArrayList.
   */
  public Menu() {
    this(new ArrayList<>());
  }

  /**
   * constructor that initialises the menu ArrayList, via a given Category.
   *
   * @param category ArrayList containing information about a category.
   */
  public Menu(@Nonnull ArrayList<Category> category) {
    this.menu = category;
  }

  /**
   * Method that adds a category to the menu.
   *
   * @param category of type Category
   */
  public void addCat(Category category) {
    menu.add(category);
  }

  /**
   * Returns the menu list.
   *
   * @return menu list.
   */
  @Nonnull
  public ArrayList<Category> getCategoryList() {
    return menu;
  }
}