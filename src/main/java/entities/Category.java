package entities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;

public class Category {

  private int categoryNumber;
  private String categoryName;
  private ArrayList<Food> items;

  public Category(int categoryNumber, @Nonnull String categoryName) {
    items = new ArrayList<Food>();
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

  @Override
  public String toString() {
    //todo fix this to use a Jackson serialiser
    return "Category{" +
        "name='" + categoryName + '\'' +
        ", category=" + items +
        '}';
  }

  @Nonnull
  public ArrayList<Food> getList() {
    return items;
  }
}