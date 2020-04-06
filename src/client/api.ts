import client from "./client";
import MenuType from "../entities/MenuType";
import MenuItem from "../entities/MenuItem";
import BasketType from "../entities/BasketType";
import CategoryType from "../entities/CategoryType";
import QueueType from "../entities/QueueType";
import OrderItem from "../entities/OrderItem";
import Ingredient from "../entities/Ingredient";
import Notification from "../entities/Notification";

export default class API {
  static async validateSession(elevated = false): Promise<boolean> {
    const endpoint = elevated ? "/restricted/orders" : "/menu";
    try {
      await client.makeRequest("GET", endpoint);
      return true;
    } catch (e) {
      return e.response.status !== 401;
    }
  }

  static async getSession(elevated = false): Promise<void> {
    const isValid = await this.validateSession(elevated);
    if (isValid) return;

    const { headers } = await client.makeRequest("GET", "/hello");
    localStorage.setItem("session", headers["x-session-id"]);
  }

  static getSocket(): WebSocket {
    return client.makeSocket();
  }

  static async login(username: string, password: string): Promise<boolean> {
    try {
      const { headers } = await client.makeRequest("POST", "/login", {
        username,
        password
      });
      localStorage.setItem("session", headers["x-session-id"]);
      return true;
    } catch {
      return false;
    }
  }

  static async fetchMenu(): Promise<MenuType> {
    const response = await client.makeRequest("GET", "/menu");
    const { categories } = response.data;

    const menu = new Map();

    categories.forEach((category: any) => {
      const { categoryName, categoryNumber, foods } = category;

      const items = foods.map(
        (item: any) =>
          new MenuItem(
            item.foodID,
            item.foodName,
            categoryNumber,
            item.foodDescription,
            item.price,
            item.ingredients.map(
              (ingredient: any) =>
                new Ingredient(
                  ingredient.ingredientID,
                  ingredient.ingredient,
                  ingredient.allergen
                )
            ),
            item.calories,
            item.imageURL ||
              "https://eda.yandex/s3/assets/fallback-pattern-9d2103a870e23618a16bcf4f8b5efa54.svg"
          )
      );

      menu.set(categoryName, items);
    });

    return menu;
  }

  static async newMenuItem(item: MenuItem): Promise<void> {
    await client.makeRequest("POST", "/restricted/newmenuitem", {
      name: item.name,
      calories: item.calories,
      category: item.category,
      description: item.description,
      price: item.price,
      ingredients: item.ingredients.map(x => x.id).join(","),
      image: null
    });
  }

  static async delMenuItem(itemID: number): Promise<void> {
    await client.makeRequest("DELETE", "/restricted/editmenu", {
      id: itemID
    });
  }

  static async callWaiter(tableID: number): Promise<void> {
    await client.makeRequest("POST", "/assist", { tableID });
  }

  static async confirmOrder(orderID: number): Promise<void> {
    await client.makeRequest("POST", "/restricted/checkorder", { id: orderID });
  }

  static async fetchBasket(): Promise<BasketType> {
    const response = await client.makeRequest("GET", "/order");
    const { items } = response.data;

    return items.flatMap((item: any) => {
      const { food, amount } = item;
      const menuItem = new MenuItem(
        food.foodID,
        food.foodName,
        0,
        food.foodDescription,
        food.price,
        [],
        0,
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

  static async getQueue(): Promise<QueueType> {
    const response = await client.makeRequest("GET", "/restricted/orders");
    const { orders } = response.data;

    const queue: QueueType = [[], [], []];

    orders.forEach((order: any) => {
      const item = new OrderItem(
        order.orderID,
        order.foodItems.flatMap(({ food, amount }: any) => {
          const menuItem = new MenuItem(
            food.foodID,
            food.foodName,
            0,
            food.foodDescription,
            0,
            [],
            0,
            ""
          );
          return Array(amount).fill(menuItem);
        }),
        new Date(order.timeOrdered),
        new Date(order.orderConfirmed),
        new Date(order.orderPreparing),
        new Date(order.orderReady),
        order.rank
      );

      switch (order.category as CategoryType) {
        case CategoryType.CONFIRMED:
        case CategoryType.ORDERED:
          queue[0].push(item);
          break;
        case CategoryType.PREPARING:
          queue[1].push(item);
          break;
        case CategoryType.READY:
          queue[2].push(item);
          break;
      }
    });

    return queue;
  }

  static async moveOrder(orderID: number, state: number): Promise<void> {
    await client.makeRequest("POST", "/restricted/update", {
      orderID: orderID,
      state: state
    });
  }

  static async getNotifications(): Promise<Array<Notification>> {
    const response = await client.makeRequest(
      "GET",
      "/restricted/staffnotifications"
    );

    return response.data.map(
      (notification: any) =>
        new Notification(
          notification.notificationID,
          notification.table
            ? `Table #${notification.table.tableNum}`
            : "Attention",
          notification.message,
          new Date(notification.time),
          notification.type,
          notification.extraData ? notification.extraData.orderID : null
        )
    );
  }

  static async getCustomerNotifications(
    tableNum: number
  ): Promise<Array<Notification>> {
    const response = await client.makeRequest(
      "GET",
      "/restricted/customernotifications",
      { tableNum: tableNum }
    );

    return response.data.map(
      (notification: any) =>
        new Notification(
          notification.notificationID,
          "Order update",
          notification.message,
          new Date(notification.time),
          notification.type,
          notification.extraData ? notification.extraData.orderID : null
        )
    );
  }

  static async delNotification(notificationID: number): Promise<void> {
    await client.makeRequest("DELETE", "/restricted/staffnotifications", {
      notificationID: notificationID
    });
  }

  static async delCustomerNotification(notificationID: number): Promise<void> {
    await client.makeRequest("DELETE", "/restricted/customernotifications", {
      notificationID: notificationID
    });
  }
}
