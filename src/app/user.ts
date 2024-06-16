export class User {
  id: string;
  username: string;
  email: string;

  constructor() {
    this.id = '';
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
