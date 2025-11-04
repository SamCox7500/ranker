import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from '../environment';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SharedRankingService {

  private usersURL: string = `${environment.apiUrl}/users`;


  constructor(private http: HttpClient) { }

  public shareRanking(userId: number, rankingId: number) : Observable<any> {
    return this.http.post<any>(`${this.usersURL}/${userId}/rankings/${rankingId}/shared`, {}, {withCredentials: true});
  }
  public unshareRanking(userId: number, rankingId: number) : Observable<any> {
    return this.http.delete<any>(`${this.usersURL}/${userId}/rankings/${rankingId}/shared`, {withCredentials: true});
  }
  public getShareInfo(userId: number, rankingId: number) : Observable<any> {
    return this.http.get<any>(`${this.usersURL}/${userId}/rankings/${rankingId}/shared`, {withCredentials: true});
  }
  public viewSharedRanking(shareToken: string) {
    return this.http.get<any>(`${environment.apiUrl}/sharedrankings/${shareToken}`, {withCredentials: true});
  }
  public lookupSharedRanking(shareToken: string) {
    return this.http.get<any>(`${environment.apiUrl}/sharedrankings/${shareToken}/lookup`, {withCredentials: true});
  }
}
