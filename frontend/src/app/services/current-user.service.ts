import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders} from '@angular/common/http';
import { User } from '../user';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { tap } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class CurrentUserService {

  private userSubject: BehaviorSubject<User | null> = new BehaviorSubject<User | null>(null);
  private user$: Observable<User | null> = this.userSubject.asObservable();

  constructor(private http: HttpClient) {}

  fetchCurrentUser(): Observable<User> {
    return this.http.get<User>('http://localhost:8080/authuser', {withCredentials: true}).pipe(
      tap(user => this.userSubject.next(user))
    );
  }

  getCurrentUser(): Observable<User | null> {
    return this.user$;
  }
  getCurrentUserValue(): User | null {
    return this.userSubject.value;
  }
  clearUser() {
    this.userSubject.next(null);
  }
}
