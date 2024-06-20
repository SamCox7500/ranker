export class User {
  id: number;
  username: string;
  email: string;

  constructor() {
    this.id = 0;
    this.username = '';
    this.email = '';
  }

  getId() {
    return this.id;
  }
  getUsername() {
    return this.username;
  }
  getEmail() {
    return this.email;
  }
}
