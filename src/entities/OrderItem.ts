import MenuItem from "./MenuItem";

export default class OrderItem {
  id: number;
  foods: Array<MenuItem>;
  timeOrdered: Date;
  timeConfirmed: Date;
  timePreparing: Date;
  timeReady: Date;
  rank: number;

  constructor(
    id: number,
    foods: Array<MenuItem>,
    timeOrdered: Date,
    timeConfirmed: Date,
    timePreparing: Date,
    timeReady: Date,
    rank: number
  ) {
    this.id = id;
    this.foods = foods;
    this.timeOrdered = timeOrdered;
    this.timeConfirmed = timeConfirmed;
    this.timePreparing = timePreparing;
    this.timeReady = timeReady;
    this.rank = rank;
  }
}
