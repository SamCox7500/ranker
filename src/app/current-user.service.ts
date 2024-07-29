import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { User } from 'user';
import { Observable } from 'rxjs/Observable';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {

  private currentUserURL: string;

  constructor(private http: HttpClient) {
    this.currentUserURL = 'http://localhost:8080/authuser'
  }

  public getCurrentUser(): Observable<User> {
    return this.http.get<User>(this.currentUserURL);
  }
}
