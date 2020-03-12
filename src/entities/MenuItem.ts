import Ingredient from "./Ingredient";

export default class MenuItem {
  id: number;
  name: string;
  category: number;
  description: string;
  price: number;
  ingredients: Array<Ingredient>;
  calories: number;
  image: string;

  constructor(
    id: number,
    name: string,
    category: number,
    description: string,
    price: number,
    ingredients: Array<Ingredient>,
    calories: number,
    image: string
  ) {
    this.id = id;
    this.name = name;
    this.category = category;
    this.description = description;
    this.price = price;
    this.ingredients = ingredients;
    this.calories = calories;
    this.image = image;
  }
}
