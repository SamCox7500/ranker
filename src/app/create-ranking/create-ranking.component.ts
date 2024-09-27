import { Component } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators} from '@angular/forms';
import { Ranking } from '../ranking';
import { ActivatedRoute, Router } from '@angular/router';
import { RankingService } from '../services/ranking.service';
import { CreateRankingDTO } from '../create-ranking-dto';
import { User } from '../user';
import { CurrentUserService } from '../services/current-user.service';

@Component({
  selector: 'app-create-ranking',
  standalone: true,
  imports: [ ReactiveFormsModule ],
  templateUrl: './create-ranking.component.html',
  styleUrl: './create-ranking.component.css'
})
export class CreateRankingComponent {
  createRankingForm = new FormGroup({
    title: new FormControl('', [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(30)]),
    description: new FormControl('', [
      Validators.required,
      Validators.minLength(1),
      Validators.maxLength(150)]),
    mediaType: new FormControl('', [
      Validators.required,
    ]),
  });

  user: User | null = null;

  ranking: Ranking = {
    id: null,
    title: '',
    description: '',
    mediaType: '',
    isReverseOrder: false,
    isPublic: false,
    userDTO: null,
  };
  //change to ranking

  constructor(private route: ActivatedRoute, private router: Router, private rankingService: RankingService, private currentUserService: CurrentUserService) {
    //this.createRankingDTO
    this.currentUserService.fetchCurrentUser().subscribe();
    this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
      this.updateCreateRankingDTO();
    });

  }
  updateCreateRankingDTO() {
    this.ranking.userDTO = this.user;
  }
  get title() {
    return this.createRankingForm.controls['title'];
  }
  get description() {
    return this.createRankingForm.controls['description'];
  }
  get mediaType() {
    return this.createRankingForm.controls['mediaType']
  }
  onSubmit() {
    if (this.createRankingForm.valid && this.user) {
      this.ranking = {
        ...this.ranking,
        title: this.title.value || '',
        description: this.description.value || '',
        mediaType: this.mediaType.value || '',
        userDTO: this.user,
      };

      this.rankingService.createRanking(this.user.id, this.ranking).subscribe({
        next: () => this.goToRankings(),
        error: err => console.log(err),
      });
    }
  }
  goToRankings() {
    this.router.navigate(['rankings']);
  }
}
