package entities;

import java.util.ArrayList;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * Class that contains all foods that are under the given category name.
 *
 * @author Oliver Graham, Jatin Khatra
 */
public class Category implements ISerialisable {
  private int categoryNumber;
  private String categoryName;
  private ArrayList<Food> items;

  /**
   * Initialises the list of foods, the category number and category name.
   *
   * @param categoryNumber category_id of the category.
   * @param categoryName   name of the category.
   */
  public Category(int categoryNumber, @Nonnull String categoryName) {
    items = new ArrayList<>();
    this.categoryName = categoryName;
    this.categoryNumber = categoryNumber;
  }

  /**
   * Returns the number of the category.
   *
   * @return number of the category.
   */
  public int getCategoryNumber() {
    return categoryNumber;
  }

  /**
   * Adds a food to the category.
   *
   * @param food Food to be added to the category.
   */
  public void addFood(Food food) {
    items.add(food);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Category category = (Category) o;

    if (categoryNumber != category.categoryNumber) {
      return false;
    }
    return Objects.equals(categoryName, category.categoryName);
  }

  @Override
  public int hashCode() {
    int result = categoryNumber;
    result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
    return result;
  }
}