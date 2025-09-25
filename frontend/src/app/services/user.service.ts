import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { User } from '../core/models/user';
import { Observable } from 'rxjs';
import { UserCredentialsDTO } from '../core/dtos/user-credentials-dto';
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
  public createUser(userCredentials: UserCredentialsDTO) {
    return this.http.post<UserCredentialsDTO>(this.usersUrl, userCredentials);
  }
  public deleteUser(id: number) {
    return this.http.delete(`${this.usersUrl}/${id}`, { withCredentials: true});
  }
}
