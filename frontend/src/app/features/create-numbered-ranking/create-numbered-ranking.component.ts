import { Component } from '@angular/core';
import { ReactiveFormsModule, FormControl, FormGroup, Validators} from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { User } from '../../core/models/user';
import { CurrentUserService } from '../../services/current-user.service';
import { CreateNumberedRankingDTO } from '../../core/dtos/create-numbered-ranking-dto';
import { NumberedRankingService } from '../../services/numbered-ranking.service';

@Component({
  selector: 'app-create-numbered-ranking',
  standalone: true,
  imports: [ ReactiveFormsModule ],
  templateUrl: './create-numbered-ranking.component.html',
  styleUrl: './create-numbered-ranking.component.css'
})
export class CreateNumberedRankingComponent {
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

  createNumRankingDTO: CreateNumberedRankingDTO = {
    title: '',
    description: '',
    mediaType: '',
    isReverseOrder: false,
    isPublic: false,
  };

  constructor(private route: ActivatedRoute, private router: Router, private numberedRankingService: NumberedRankingService, private currentUserService: CurrentUserService) {
    this.currentUserService.fetchCurrentUser().subscribe();
    this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
    });

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
      this.createNumRankingDTO = {
        ...this.createNumRankingDTO,
        title: this.title.value || '',
        description: this.description.value || '',
        mediaType: this.mediaType.value || '',
      };

      this.numberedRankingService.createRanking(this.user.id, this.createNumRankingDTO).subscribe({
        next: () => this.goToRankings(),
        error: err => console.log(err),
      });
    }
  }
  goToRankings() {
    this.router.navigate(['rankings']);
  }
}
