import Ingredient from "./Ingredient";

export default class MenuItem {
  id: number;
  name: string;
  description: string;
  price: number;
  ingredients: Array<Ingredient>;
  image: string;

  constructor(
    id: number,
    name: string,
    description: string,
    price: number,
    ingredients: Array<Ingredient>,
    image: string
  ) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.ingredients = ingredients;
    this.image = image;
  }
}
