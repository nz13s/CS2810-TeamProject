import client from "./client";
import MenuType from "../entities/MenuType";
import MenuItem from "../entities/MenuItem";
import BasketType from "../entities/BasketType";

export default class API {
  static async validateSession(): Promise<boolean> {
    try {
      await client.makeRequest("GET", "/menu");
      return true;
    } catch (e) {
      return e.response.status !== 401;
    }
  }

  static async getSession(): Promise<void> {
    const isValid = await this.validateSession();
    if (isValid) return;

    const { headers } = await client.makeRequest("GET", "/hello");
    localStorage.setItem("session", headers["x-session-id"]);
  }

  static async fetchMenu(): Promise<MenuType> {
    const response = await client.makeRequest("GET", "/menu");
    const { categories } = response.data;

    const menu = new Map();

    categories.forEach((category: any) => {
      const { categoryName, list } = category;

      const items = list.map(
        (item: any) =>
          new MenuItem(
            item.foodID,
            item.foodName,
            item.foodDescription,
            item.price,
            "https://d1ralsognjng37.cloudfront.net/b9b225fe-fc45-4170-b217-78863c2de64e"
          )
      );

      menu.set(categoryName, items);
    });

    return menu;
  }

  static async fetchBasket(): Promise<BasketType> {
    const response = await client.makeRequest("GET", "/order");
    const { items } = response.data;

    return items.flatMap((item: any) => {
      const { food, amount } = item;
      const menuItem = new MenuItem(
        food.foodID,
        food.foodName,
        food.foodDescription,
        food.price,
        ""
      );
      return Array(amount).fill(menuItem);
    });
  }

  static async saveBasket(tableID: number): Promise<void> {
    await client.makeRequest("POST", "/save", {
      table_num: tableID // eslint-disable-line
    });
  }

  static async addToBasket(item: MenuItem): Promise<void> {
    await client.makeRequest("POST", "/order", {
      item: item.id,
      count: 1
    });
  }

  static async delFromBasket(item: MenuItem): Promise<void> {
    await client.makeRequest("DELETE", "/order", {
      item: item.id,
      count: 1
    });
  }
}
