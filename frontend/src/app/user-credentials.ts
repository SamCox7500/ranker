export class UserCredentials {

  username: string;
  password: string;

  constructor() {
    this.username = '';
    this.password = '';
  }

  setUsername(username: string) {
    this.username = username;
  }

  setPassword(password: string) {
    this.password = password;
  }

  getUsername() {
    return this.username;
  }

  getPassword() {
    return this.password;
  }

}
