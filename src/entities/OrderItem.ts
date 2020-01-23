export default class OrderItem {
  id: number;
  name: string;
  ordered: Date;

  constructor(id: number, name: string, ordered: Date) {
    this.id = id;
    this.name = name;
    this.ordered = ordered;
  }
}
