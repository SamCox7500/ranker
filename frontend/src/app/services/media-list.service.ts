import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MediaListEntry } from '../media-list-entry';
import { EntryAddRequestDTO } from '../entry-add-request-dto';
import { EntryMoveRequestDTO } from '../entry-move-request-dto';
import { MediaList } from '../media-list';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class MediaListService {

  private usersUrl: string = `${environment.apiUrl}/users`;

  constructor(private http: HttpClient) { }

  public getMediaList(userId: number, rankingId: number): Observable<MediaList> {
    return this.http.get<MediaList>(`${this.usersUrl}/${userId}/numberedrankings/${rankingId}/medialist`, { withCredentials: true });
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
