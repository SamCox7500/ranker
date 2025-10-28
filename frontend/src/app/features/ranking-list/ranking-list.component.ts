import { Component } from '@angular/core';
import { CurrentUserService } from '../../services/current-user.service';
import { RankingService } from '../../services/ranking.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../core/models/user';
import { Ranking } from '../../core/models/ranking';
import { Subscription } from 'rxjs';
import { OnDestroy } from '@angular/core';
import { NumberedRankingService } from '../../services/numbered-ranking.service';

@Component({
  selector: 'app-ranking-list',
  standalone: true,
  imports: [],
  templateUrl: './ranking-list.component.html',
  styleUrl: './ranking-list.component.css'
})
export class RankingListComponent {

  user: User | null = null;
  rankings: Ranking[] = [];

  private subscriptions: Subscription = new Subscription();

  constructor(private route: ActivatedRoute, private router: Router, private rankingService: RankingService, private currentUserService: CurrentUserService, private numberedRankingService: NumberedRankingService) { }

  ngOnInit(): void {
    const currentUserSub = this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
      if (!user) {
        const fetchUserSub = this.currentUserService.fetchCurrentUser().subscribe();
        this.subscriptions.add(fetchUserSub);
      }
      this.refreshRanking();
    });
    this.subscriptions.add(currentUserSub);
  }
  goToCreateNumRanking(): void {
    this.router.navigate(['createnumranking']);
  }
  goToRanking(rankingId: number | null, rankingType: string): void {
    if (rankingId && this.user?.id) {
      if (rankingType === 'NUMBERED_RANKING') {
        this.router.navigate(['/users', this.user.id, 'numberedrankings', rankingId]);
      }
    }
  }
  deleteRanking(rankingId: number | null, rankingType: string): void {
    if (rankingId && this.user) {
      if (rankingType === 'NUMBERED_RANKING') {
        const deleteRankingSub = this.numberedRankingService.deleteRanking(this.user.id, rankingId).subscribe({
        next: () => {
          this.rankings = this.rankings.filter(ranking => ranking.id !== rankingId);
          this.refreshRanking();
        },
        error: err => console.log(err)
      });
      this.subscriptions.add(deleteRankingSub);
      }
    }
  }
  refreshRanking(): void {
    if (this.user) {
      const getRankingsSub = this.rankingService.getAllRankings(this.user.id).subscribe((data: Ranking[]) => {
        this.rankings = data;
      });
      this.subscriptions.add(getRankingsSub);
    }
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
}
