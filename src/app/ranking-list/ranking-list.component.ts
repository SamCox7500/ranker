import { Component } from '@angular/core';
import { CurrentUserService} from '../services/current-user.service';
import { RankingService } from '../services/ranking.service';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../user';
import { Ranking } from '../ranking';

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

  constructor(private route: ActivatedRoute, private router: Router, private rankingService: RankingService, private currentUserService: CurrentUserService) {}

  ngOnInit(): void {
    this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
      if (!user) {
        this.currentUserService.fetchCurrentUser().subscribe();
      }
    });
    if (this.user) {
      this.rankingService.getAllRankings(this.user.id).subscribe((data: Ranking[]) => {
        this.rankings = data;
      });
    }
  }
  goToCreateRanking(): void {
    this.router.navigate(['createranking']);
  }
  goToMediaList(rankingId: number | null) : void {
    if (rankingId) {
      this.router.navigate(['/medialist', rankingId]);
    }
  }
}
