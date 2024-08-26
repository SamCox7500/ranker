import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Ranking } from '../ranking';

@Injectable({
  providedIn: 'root'
})
export class RankingService {

  private usersUrl: string = 'http://localhost:8080/users';

  constructor(private http: HttpClient) { }

  public getRankingsByUserId(userId: number): Observable<Ranking[]> {
    return this.http.get<Ranking[]>(`${this.usersUrl}/${userId}/${'numberedrankings'}`, {withCredentials: true});
  }
}
