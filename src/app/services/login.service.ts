import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserCredentials } from '../user-credentials';
import { Observable, tap } from 'rxjs';
import { CurrentUserService } from './current-user.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginUrl: string = 'http://localhost:8080/login';
  private logoutUrl: string = 'http://localhost:8080/logout';

  constructor(private http: HttpClient, private currentUserService: CurrentUserService) {}

  public login(userCredentials: UserCredentials): Observable<any> {
    return this.http.post(this.loginUrl, userCredentials, { withCredentials: true}).pipe(
      tap(() => {
        this.currentUserService.fetchCurrentUser().subscribe();
      })
    );
  }

  public logout(): Observable<any> {
    this.currentUserService.clearUser();
    return this.http.post(this.logoutUrl, {}, { withCredentials: true});
  }
}
