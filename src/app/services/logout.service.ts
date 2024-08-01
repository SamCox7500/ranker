import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class LogoutService {

  private logoutUrl: string = "http://localhost:8080/logout";

  constructor(private httpClient: HttpClient) { }

  public logout() {
    return this.httpClient.post(logoutUrl, {}, { withCredentials: true });
  }
}
