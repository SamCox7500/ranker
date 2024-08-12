import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { User } from '../user';
import { Observable } from 'rxjs';
import { map } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {

  private currentUserURL: string = 'http://localhost:8080/authuser';
  private usersURL: string = 'http://localhost:8080/users';

  constructor(private http: HttpClient) {}

  public getCurrentUser() : Observable<User> {
    return this.http.get<User>(this.currentUserURL, { withCredentials: true });
  }
}
