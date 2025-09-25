import { Component } from '@angular/core';
import { CurrentUserService } from '../../services/current-user.service';
import { RankingService } from '../../services/ranking.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../core/models/user';
import { Ranking } from '../../core/models/ranking';
import { Subscription } from 'rxjs';
import { OnDestroy } from '@angular/core';

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

  constructor(private route: ActivatedRoute, private router: Router, private rankingService: RankingService, private currentUserService: CurrentUserService) { }

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
  goToCreateRanking(): void {
    this.router.navigate(['createranking']);
  }
  goToMediaList(rankingId: number | null): void {
    if (rankingId) {
      this.router.navigate(['/medialist', rankingId]);
    }
  }
  deleteRanking(rankingId: number | null): void {
    if (rankingId && this.user) {
      const deleteRankingSub = this.rankingService.deleteRanking(this.user.id, rankingId).subscribe({
        next: () => {
          this.rankings = this.rankings.filter(ranking => ranking.id !== rankingId);
          this.refreshRanking();
        },
        error: err => console.log(err)
      });
      this.subscriptions.add(deleteRankingSub);
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
