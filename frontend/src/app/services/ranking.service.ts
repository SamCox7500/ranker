import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Ranking } from '../ranking';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class RankingService {

  private usersUrl: string = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) { }

  public getAllRankings(userId: number): Observable<Ranking[]> {
    return this.http.get<Ranking[]>(`${this.usersUrl}/${userId}/numberedrankings`, {withCredentials: true});
  }
  public getRanking(userId: number, rankingId: number) : Observable<Ranking> {
    return this.http.get<Ranking>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}`, {withCredentials: true});
  }
  public createRanking(userId: number, ranking: Ranking) : Observable<void> {
    return this.http.post<void>(`${this.usersUrl}/${userId}/numberedrankings`, ranking, {withCredentials: true});
  }
  public updateRanking(userId: number, rankingId: number, ranking: Ranking) : Observable<void> {
    return this.http.put<void>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}`, ranking, {withCredentials: true});
  }
  public deleteRanking(userId: number, rankingId: number) : Observable<void> {
    return this.http.delete<void>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}`, {withCredentials: true});
  }
}
