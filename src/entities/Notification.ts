export default class Notification {
  id: number;
  title: string;
  content: string;
  time: Date;
  kind: string;
  extra: any;

  constructor(
    id: number,
    title: string,
    content: string,
    time: Date,
    kind: string,
    extra: any
  ) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.time = time;
    this.kind = kind;
    this.extra = extra;
  }
}
