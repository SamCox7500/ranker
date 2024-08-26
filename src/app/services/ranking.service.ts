import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RankingService {

  private usersUrl: string = 'http://localhost:8080/users';

  constructor() { }

  public getRankingsByUserId(userId: number, rankingId: number): Observable<Ranking[]> {
    return this.http.get<Ranking>(`${this.usersUrl}/${userId}/${'rankings'}`, {withCredentials: true});
  }
}
