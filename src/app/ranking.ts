export class Ranking {
  id: number;
  user: User;
  title: string;
  desc: title;
  isPublic: boolean;
  isReverseOrder: boolean;

  constructor(id: number, user: User, title: string, desc: string, boolean: isPublic, boolean: isReverseOrder) {
    this.id = id;
    this.user = user;
    this.title = title;
    this.desc = desc;
    this.isPublic = isPublic;
    this.isReverseOrder = isReverseOrder;
  }
}
