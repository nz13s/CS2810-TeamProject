package entities;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class Menu {

  private ArrayList<Category> menu;

  public Menu() {
    this(new ArrayList<>());
  }

  public Menu(@Nonnull ArrayList<Category> categories){
    this.menu = categories;
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
    //todo fix this to use a Jackson serialiser
    return "Menu{" +
        "menu=" + menu +
        '}';
  }

  @Nonnull
  public ArrayList<Category> getList() {
    return menu;
  }
}