import { User } from './user';

export class Ranking {
  id: number;
  user: User;
  title: string;
  description: string;
  isPublic: boolean;
  isReverseOrder: boolean;

  constructor(id: number, user: User, title: string, description: string, isPublic: boolean, isReverseOrder: boolean) {
    this.id = id;
    this.user = user;
    this.title = title;
    this.description = description;
    this.isPublic = isPublic;
    this.isReverseOrder = isReverseOrder;
  }
}
