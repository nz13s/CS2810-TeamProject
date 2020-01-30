import MenuItem from "./MenuItem";

export default class OrderItem {
  id: number;
  foods: Array<MenuItem>;
  ordered: Date;
  rank: number;

  constructor(id: number, foods: Array<MenuItem>, ordered: Date, rank: number) {
    this.id = id;
    this.foods = foods;
    this.ordered = ordered;
    this.rank = rank;
  }
}
