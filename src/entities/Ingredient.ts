export default class Ingredient {
  id: number;
  name: string;
  allergen: boolean;

  constructor(id: number, name: string, allergen: boolean) {
    this.id = id;
    this.name = name;
    this.allergen = allergen;
  }
}
