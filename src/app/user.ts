export class User {
  id: number;
  username: string;

  constructor() {
    this.id = 0;
    this.username = '';
  }

  getId() {
    return this.id;
  }
  getUsername() {
    return this.username;
  }
}
