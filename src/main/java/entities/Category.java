package entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Objects;

public class Category {

  private int categoryNumber;
  private String categoryName;
  private ArrayList<Food> items;

  public Category(int categoryNumber, @Nonnull String categoryName) {
    items = new ArrayList<>();
    this.categoryName = categoryName;
    this.categoryNumber = categoryNumber;
  }

  public int getCategoryNumber() {
    return categoryNumber;
  }

  @Nonnull
  public String getCategoryName() {
    return categoryName;
  }

  public void addFood(Food food) {
    items.add(food);
  }

  public int size() {
    return items.size();
  }

  @Nullable
  public Food getFood(int i) {
    try {
      return items.get(i);
    } catch (IndexOutOfBoundsException ex){
      return null;
    }
  }

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