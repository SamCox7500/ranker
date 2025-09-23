import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { environment } from '../environment';
import { NumberedRanking } from '../numbered-ranking';
import { EntryAddRequestDTO } from '../entry-add-request-dto';
import { EntryMoveRequestDTO } from '../entry-move-request-dto';
import { CreateNumberedRankingDTO } from '../create-numbered-ranking-dto';

@Injectable({
  providedIn: 'root'
})
export class NumberedRankingService {

  private usersUrl: string = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) { }

  public getRanking(userId: number, rankingId: number) : Observable<NumberedRanking> {
    return this.http.get<NumberedRanking>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}`, {withCredentials: true});
  }
  public createRanking(userId: number, createNumRankingDTO: CreateNumberedRankingDTO) : Observable<void> {
    return this.http.post<void>(`${this.usersUrl}/${userId}/numberedrankings`, createNumRankingDTO, {withCredentials: true});
  }
  public updateRanking(userId: number, rankingId: number, ranking: NumberedRanking) : Observable<void> {
    return this.http.put<void>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}`, ranking, {withCredentials: true});
  }
  public deleteRanking(userId: number, rankingId: number) : Observable<void> {
    return this.http.delete<void>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}`, {withCredentials: true});
  }
  public addEntry(userId: number, rankingId: number, addRequest: EntryAddRequestDTO): Observable<void> {
    return this.http.post<void>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}/medialist/entries`, addRequest, { withCredentials: true });
  }
  public moveEntry(userId: number, rankingId: number, entryId: number, moveRequest: EntryMoveRequestDTO) {
    return this.http.put<void>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}/medialist/entries/${entryId}`,
      moveRequest, { withCredentials: true });
  }
  public deleteEntry(userId: number, rankingId: number, entryId: number): Observable<void> {
    return this.http.delete<void>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}/medialist/entries/${entryId}`, { withCredentials: true });
  }
  public deleteEntries(userId: number, rankingId: number, entryIds: number[]): Observable<void> {
    return this.http.delete<void>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}/medialist/entries`, { body: entryIds, withCredentials: true });
  }
}
