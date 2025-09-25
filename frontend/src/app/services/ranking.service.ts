import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Ranking } from '../core/models/ranking';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class RankingService {

  private usersUrl: string = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) { }

  public getAllRankings(userId: number): Observable<Ranking[]> {
    return this.http.get<Ranking[]>(`${this.usersUrl}/${userId}/rankings`, {withCredentials: true});
  }
  public getRanking(userId: number, rankingId: number) : Observable<Ranking> {
    return this.http.get<Ranking>(`${this.usersUrl}/${userId}/rankings/${rankingId}`, {withCredentials: true});
  }
  public createRanking(userId: number, ranking: Ranking) : Observable<void> {
    return this.http.post<void>(`${this.usersUrl}/${userId}/rankings`, ranking, {withCredentials: true});
  }
  public updateRanking(userId: number, rankingId: number, ranking: Ranking) : Observable<void> {
    return this.http.put<void>(`${this.usersUrl}/${userId}/rankings/${rankingId}`, ranking, {withCredentials: true});
  }
  public deleteRanking(userId: number, rankingId: number) : Observable<void> {
    return this.http.delete<void>(`${this.usersUrl}/${userId}/rankings/${rankingId}`, {withCredentials: true});
  }
}
