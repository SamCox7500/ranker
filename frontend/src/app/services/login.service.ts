import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { UserCredentials } from '../user-credentials';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { CurrentUserService } from './current-user.service';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  private loginUrl: string = `${environment.apiUrl}/login`;
  private logoutUrl: string = `${environment.apiUrl}/logout`;
  private authenticatedSubject: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);
  authenticated$: Observable<boolean> = this.authenticatedSubject.asObservable();

  authenticated: boolean;

  constructor(private http: HttpClient, private currentUserService: CurrentUserService) {
    this.authenticated = false;
  }

  public login(userCredentials: UserCredentials): Observable<any> {
    return this.http.post(this.loginUrl, userCredentials, { withCredentials: true}).pipe(
      tap(() => {
        this.currentUserService.fetchCurrentUser().subscribe();
        this.authenticatedSubject.next(true);
      })
    );
  }

  public logout(): Observable<any> {
    this.currentUserService.clearUser();
    this.authenticatedSubject.next(false);
    return this.http.post(this.logoutUrl, {}, { withCredentials: true});
  }
}
