import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MediaListService } from '../services/media-list.service';
import { MediaList } from '../media-list';
import { CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray } from '@angular/cdk/drag-drop'
import { MediaListEntry } from '../media-list-entry';
import { NumberedRanking } from '../numbered-ranking';
import { User } from '../user';
import { CurrentUserService } from '../services/current-user.service';
import { MovieEntry } from '../movie-entry';
import { TVShowEntry } from '../tvshow-entry';
import { EntryMoveRequestDTO } from '../entry-move-request-dto';
import { Observable, Subscription, switchMap } from 'rxjs';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NumberedRankingService } from '../services/numbered-ranking.service';

@Component({
  selector: 'app-numbered-ranking',
  standalone: true,
  imports: [CdkDropList, CdkDrag, ReactiveFormsModule],
  templateUrl: './numbered-ranking.component.html',
  styleUrl: './numbered-ranking.component.css'
})
export class NumberedRankingComponent {
  //mediaList: MediaList;

  rankingId: number | null = null;
  ranking: NumberedRanking | null = null;
  user: User | null = null;

  isEditMode: boolean = false;

  rankingForm: FormGroup;

  private subscriptions: Subscription = new Subscription();

  readonly TMDB_IMAGE_BASE_URL = 'https://image.tmdb.org/t/p/';
  readonly TMDB_IMAGE_SIZE = 'w92';


  constructor(private route: ActivatedRoute, private router: Router, private currentUserService: CurrentUserService, private fb: FormBuilder, private rankingService: NumberedRankingService) {
    this.rankingForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(30)]],
      description: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(150)]],
    });
  }

  toggleEditMode(): void {

    if (this.isEditMode) {
      if (this.rankingForm.valid && this.ranking) {
        const updatedRanking = {
          ...this.ranking,
          title: this.rankingForm.value.title,
          description: this.rankingForm.value.description
        };

        if (this.user && this.rankingId) {
          const updateRankingSub = this.rankingService.updateRanking(this.user.id, this.rankingId, updatedRanking).subscribe({
            next: (response) => {
              this.ranking = updatedRanking;
            },
            error: (err) => {
              console.log(err);
            }
          });
          this.subscriptions.add(updateRankingSub);
        }

      }
    } else {
      if (this.ranking) {
        this.rankingForm.patchValue({
          title: this.ranking.title,
          description: this.ranking.description
        });
      }
    }

    if (this.rankingForm.valid) {
      this.isEditMode = !this.isEditMode;
    }
  }


  ngOnInit(): void {

    this.rankingId = Number(this.route.snapshot.paramMap.get('rankingId'));
    this.currentUserService.fetchCurrentUser();
    const userSub = this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;

      if (this.user && this.rankingId) {
        const numRankingSub = this.rankingService.getRanking(this.user.id, this.rankingId).subscribe((numRanking: NumberedRanking) => {

          this.ranking = numRanking;

          // Initializing the ranking form
          this.rankingForm.patchValue({
            title: this.ranking.title,
            description: this.ranking.description
          });
        });
        this.subscriptions.add(numRankingSub);
      } else {
        console.log(this.user?.id);
        console.log(this.rankingId)
      }
    });
    this.subscriptions.add(userSub);
  }

  drop(event: CdkDragDrop<MediaListEntry[]>) {
    if (!this.isEditMode || !this.ranking?.mediaListDTO?.entries) {
      return;
    }

    const entries = this.ranking.mediaListDTO.entries;

    moveItemInArray(entries, event.previousIndex, event.currentIndex);
    this.updateRankings();

    const movedEntry = entries[event.currentIndex];
    if (this.user && this.rankingId && movedEntry) {
      const moveRequest: EntryMoveRequestDTO = { 
        entryId: movedEntry.id,
        newPosition: movedEntry.ranking
      };
      console.log(moveRequest);
      this.rankingService.moveEntry(this.user.id, this.rankingId, movedEntry.id, moveRequest).subscribe({
        next: () => console.log('Ranking updated successfully'),
        error: err => console.log(err),
      });
    }
  }

  isMovieEntry(entry: MediaListEntry): entry is MovieEntry {
    return (entry as MovieEntry).title !== undefined;
  }
  isTVShowEntry(entry: MediaListEntry): entry is TVShowEntry {
    return (entry as TVShowEntry).name !== undefined;
  }
  goToAddMedia() {
    if (!this.ranking) {
      return;
    }
    this.router.navigate(['add-media', this.rankingId, this.ranking?.mediaListDTO.entries.length + 1, this.ranking?.mediaType]);
  }
  updateRankings(): void {
    if (!this.ranking?.mediaListDTO?.entries) return;

    this.ranking.mediaListDTO.entries.forEach((entry, index) => {
      entry.ranking = index + 1;
    });
  }
  removeEntry(entryId: number): void {
    if (!this.user || !this.rankingId || !entryId) {
      return;
    }
    this.rankingService
      .deleteEntry(this.user.id, this.rankingId, entryId)
      .subscribe({
        next: () => {
          const entries = this.ranking!.mediaListDTO.entries;
          const index = entries.findIndex((entry) => entry.id === entryId);
          if (index !== -1) {
            entries.splice(index, 1);
            this.updateRankings();
          }
        },
        error: (err) => console.log(err),
      });
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
  get title() {
    return this.rankingForm.controls['title'];
  }
  get description() {
    return this.rankingForm.controls['description'];
  }
  getPosterUrl(posterPath: string): string {
    return `${this.TMDB_IMAGE_BASE_URL}${this.TMDB_IMAGE_SIZE}${posterPath}`;
  }
}
