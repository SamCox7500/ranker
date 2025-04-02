import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MediaListService } from '../services/media-list.service';
import { MediaList } from '../media-list';
import { CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray } from '@angular/cdk/drag-drop'
import { MediaListEntry } from '../media-list-entry';
import { Ranking } from '../ranking';
import { User } from '../user';
import { CurrentUserService } from '../services/current-user.service';
import { MovieEntry } from '../movie-entry';
import { TVShowEntry } from '../tvshow-entry';
import { EntryMoveRequestDTO } from '../entry-move-request-dto';
import { Observable, Subscription, switchMap } from 'rxjs';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RankingService } from '../services/ranking.service';

@Component({
  selector: 'app-media-list',
  standalone: true,
  imports: [CdkDropList, CdkDrag, ReactiveFormsModule],
  templateUrl: './media-list.component.html',
  styleUrl: './media-list.component.css'
})
export class MediaListComponent implements OnInit, OnDestroy {


  //mediaList: MediaList;

  rankingId: number | null = null;
  mediaType: string = '';
  mediaListEntries: MediaListEntry[] = [];
  ranking: Ranking | null = null;
  user: User | null = null;
  isEditMode: boolean = false;

  rankingForm: FormGroup;

  private subscriptions: Subscription = new Subscription();

  readonly TMDB_IMAGE_BASE_URL = 'https://image.tmdb.org/t/p/';
  readonly TMDB_IMAGE_SIZE = 'w92';


  constructor(private route: ActivatedRoute, private mediaListService: MediaListService, private router: Router, private currentUserService: CurrentUserService, private fb: FormBuilder, private rankingService: RankingService) {
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
        const mediaListSub = this.mediaListService.getMediaList(this.user.id, this.rankingId).subscribe((mediaList: MediaList) => {

          this.mediaType = mediaList.mediaType;
          this.mediaListEntries = mediaList.mediaListEntryDTOList;
          this.ranking = mediaList.numberedRankingDTO;
          this.mediaListEntries.sort((a, b) => a.ranking - b.ranking);

          // Initializing the ranking form
          this.rankingForm.patchValue({
            title: this.ranking.title,
            description: this.ranking.description
          });
        });
        this.subscriptions.add(mediaListSub);
      } else {
        console.log(this.user?.id);
        console.log(this.rankingId)
      }
    });
    this.subscriptions.add(userSub);
  }

  drop(event: CdkDragDrop<MediaListEntry[]>) {
    if (!this.isEditMode) {
      return;
    }

    moveItemInArray(this.mediaListEntries, event.previousIndex, event.currentIndex);
    this.updateRankings();

    const movedEntry = this.mediaListEntries[event.currentIndex];
    if (this.user && this.rankingId && movedEntry) {
      const moveRequest: EntryMoveRequestDTO = { entryId: movedEntry.id, newPosition: movedEntry.ranking };
      console.log(moveRequest);
      this.mediaListService.moveEntry(this.user.id, this.rankingId, movedEntry.id, moveRequest).subscribe({
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
    this.router.navigate(['add-media', this.rankingId, this.mediaListEntries.length + 1, this.mediaType]);
  }
  updateRankings(): void {
    this.mediaListEntries.forEach((entry, index) => {
      entry.ranking = index + 1;
    });
  }
  removeEntry(entryId: number): void {
    if (this.user && this.rankingId && entryId) {
      this.mediaListService.deleteEntry(this.user.id, this.rankingId, entryId).subscribe({
        next: () => {
          const entryToRemoveIndex = this.mediaListEntries.findIndex((entry) => entry.id === entryId);

          if (entryToRemoveIndex !== -1) {
            this.mediaListEntries.splice(entryToRemoveIndex, 1);
            this.updateRankings();
          }
        },
        error: (err) => console.log(err),
      });
    }
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
