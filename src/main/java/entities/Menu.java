package entities;

import javax.annotation.Nonnull;
import java.util.ArrayList;

/**
 * Class that stores all categories of the food menu.
 *
 * @author Jatin
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

  public Menu(@Nonnull ArrayList<Category> category){
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
   * Returns the size of the menu ArrayList.
   *
   * @return size of menu.
   */

  public int size() {
    return menu.size();
  }

  /**
   * Returns a category from a given index.
   *
   * @param i representing the index.
   * @return category from given i.
   */

  public Category getCat(int i) {
    return menu.get(i);
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