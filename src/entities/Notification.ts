export default class Notification {
  id: number;
  title: string;
  content: string;
  time: Date;

  constructor(id: number, title: string, content: string, time: Date) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.time = time;
  }
}
