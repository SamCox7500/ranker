import { ActivatedRouteSnapshot, ResolveFn } from '@angular/router';
import { NumberedRanking } from '../core/models/numbered-ranking';
import { catchError, of, map } from 'rxjs';
import { inject } from '@angular/core';
import { RankingService } from '../services/ranking.service';
import { SharedRankingService } from '../services/shared-ranking.service';
import { NumberedRankingService } from '../services/numbered-ranking.service';

export interface NumberedRankingResolverData {
  ranking: NumberedRanking | null;
  //isOwner: boolean;
  canEdit: boolean;
  userId: number | null;
}

export const numberedRankingResolver: ResolveFn<NumberedRankingResolverData> = (route: ActivatedRouteSnapshot) => {

  const numRankingService = inject(NumberedRankingService);
  const sharedRankingService = inject(SharedRankingService);

  const userId = Number(route.paramMap.get('userId'));
  const rankingId = Number(route.paramMap.get('rankingId'));
  const shareToken = route.paramMap.get('token');

  //If there's a token then it's a shared ranking
  if (shareToken) {
    return sharedRankingService.viewSharedRanking(shareToken).pipe(
      map(ranking => ({
        ranking,
        //isOwner: false,
        //canEdit: !!ranking?.permissions?.includes('EDIT'),
        canEdit: false,
        userId: null,
      })),
      catchError(() => of ({ ranking: null, canEdit: false, userId: null }))
    );
  }
  //If ID is present then it is the owner accessing their own ranking
  if (rankingId && userId) {
    return numRankingService.getRanking(userId, rankingId).pipe(
      map(ranking => ({
        ranking,
        userId: userId,
        canEdit: true,
      })),
      catchError(() => of({ ranking: null, canEdit: false, userId: null }))
    );
  }
  return of({ ranking: null, canEdit: false, userId: null});
};
