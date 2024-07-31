import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserCredentials } from '../user-credentials';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginUrl: string;

  constructor(private http: HttpClient) {
    this.loginUrl = 'http://localhost:8080/login'
  }

  public login(userCredentials: UserCredentials): Observable<any> {
    return this.http.post(this.loginUrl, userCredentials, { withCredentials: true});
  }

  public logout(): Observable<any> {
    return this.http.post('http://localhost:8080/logout', {}, { withCredentials: true});
  }
}
