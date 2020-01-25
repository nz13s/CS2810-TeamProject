package entities;

import java.util.ArrayList;

public class Menu {

  private ArrayList<Category> menu;

  public Menu() {
    menu = new ArrayList<Category>();
  }

  public void addCat(Category category) {
    menu.add(category);
  }

  public int size() {
    return menu.size();
  }

  public Category getCat(int i) {
    return menu.get(i);
  }

  @Override
  public String toString() {
    return "Menu{" +
        "menu=" + menu +
        '}';
  }

  public ArrayList<Category> getList() {
    return menu;
  }
}