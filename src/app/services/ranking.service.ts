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
  public getRankingByIdAndUser(rankingId: number, userId: number) : Observable<Ranking> {
    return this.http.get<Ranking>(`${this.usersUrl}/${userId}/${'numberedrankings'}/${rankingId}`, {withCredentials: true});
  }
  public createRanking(userId: number) {
    return this.http.post<Ranking>(`${this.usersUrl}/${userId}/${'numberedrankings'}`, {withCredentials: true});
  }
  public updateRanking(userId: number, rankingId: number) {
    return this.http.put<Ranking>(`${this.usersUrl}/${userId}/${'numberedrankings'}/${rankingId}`, {withCredentials: true});
  }
  public deleteRanking(userId: number, rankingId: number) {
    return this.http.delete<Ranking>(`${this.usersUrl}/${userId}/${'numberedrankings'}/${rankingId}`, {withCredentials: true});
  }
}
