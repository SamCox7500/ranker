import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../user';
import { Observable } from 'rxjs';
import { UserCredentials } from '../user-credentials';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private usersUrl: string = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) {
  }

  public getUsers(): Observable<User[]> {
    return this.http.get<User[]>(this.usersUrl, { withCredentials: true});
  }
  public getUserById(id: number): Observable<User> {
    return this.http.get<User>(`${this.usersUrl}/${id}`, { withCredentials: true});
  }
  /*
  public getUserByUsername(username: string): Observable<User> {
    return this.http.get<User>(`${this.usersUrl}/${'?username='}/${username}`);
  }
  */
  public createUser(userCredentials: UserCredentials) {
    return this.http.post<UserCredentials>(this.usersUrl, userCredentials);
  }
  public deleteUser(id: number) {
    return this.http.delete(`${this.usersUrl}/${id}`, { withCredentials: true});
  }
}
