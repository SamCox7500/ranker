import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../user';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersUrl: string;

  constructor(private http: HttpClient) {
    this.usersUrl = 'http://localhost:8080/users';
  }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl);
  }
  public getUserById(id: number):
  public createUser(userCredentials: UserCredentials) {
    return this.http.post<UserCredentials>(this.usersUrl, userCredentials);
  }
}
