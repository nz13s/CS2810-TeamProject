package entities;

import java.util.ArrayList;

public class Category {

  private int categoryNumber;
  private String categoryName;
  private ArrayList<Food> items;

  public Category(int categoryNumber, String categoryName) {
    items = new ArrayList<Food>();
    this.categoryName = categoryName;
    this.categoryNumber = categoryNumber;
  }

  public int getCategoryNumber() {
    return categoryNumber;
  }

  public String getCategoryName() {
    return categoryName;
  }

  public void addFood(Food food) {
    items.add(food);
  }

  public int size() {
    return items.size();
  }

  public Food getFood(int i) {
    return items.get(i);
  }

  @Override
  public String toString() {
    return "Category{" +
        "name='" + categoryName + '\'' +
        ", category=" + items +
        '}';
  }

  public ArrayList<Food> getList() {
    return items;
  }
}