package entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;


/**
 * Class that contains all foods that are under the given category name.
 */

public class Category {

  private int categoryNumber;
  private String categoryName;
  private ArrayList<Food> items;

  /**
   * Initialises the list of foods, the category number and category name.
   *
   * @param categoryNumber category_id of the category.
   * @param categoryName name of the category.
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
   * Returns the category's name.
   *
   * @return category's name.
   */

  @Nonnull
  public String getCategoryName() {
    return categoryName;
  }

  /**
   * Adds a food to the category.
   *
   * @param food Food to be added to the category.
   */

  public void addFood(Food food) {
    items.add(food);
  }

  /**
   * Returns the size of the category.
   *
   * @return size of the category.
   */

  public int size() {
    return items.size();
  }

  /**
   * Gets a Food object from a given index i.
   *
   * @param i index of the Food
   * @return Food from index i.
   */

  @Nullable
  public Food getFood(int i) {
    try {
      return items.get(i);
    } catch (IndexOutOfBoundsException ex){
      return null;
    }
  }

  /**
   * Returns all Food objects inside the Category.
   *
   * @return Food objects inside the Category.
   */

  @Nonnull
  public ArrayList<Food> getList() {
    return items;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Category category = (Category) o;

    if (categoryNumber != category.categoryNumber) return false;
    return Objects.equals(categoryName, category.categoryName);
  }

  @Override
  public int hashCode() {
    int result = categoryNumber;
    result = 31 * result + (categoryName != null ? categoryName.hashCode() : 0);
    return result;
  }
}