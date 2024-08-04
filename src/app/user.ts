export class User {
  id: number;
  username: string;

  constructor(id: number, username: string) {
    this.id = id;
    this.username = username;
  }
  getId() {
    return this.id;
  }
  getUsername() {
    return this.username;
  }
}
