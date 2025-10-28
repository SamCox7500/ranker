import { Component, OnDestroy, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MediaListService } from '../../services/media-list.service';
import { MediaList } from '../../core/models/media-list';
import { CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray } from '@angular/cdk/drag-drop'
import { MediaListEntry } from '../../core/models/media-list-entry';
import { NumberedRanking } from '../../core/models/numbered-ranking';
import { User } from '../../core/models/user';
import { CurrentUserService } from '../../services/current-user.service';
import { MovieEntry } from '../../core/models/movie-entry';
import { TVShowEntry } from '../../core/models/tvshow-entry';
import { EntryMoveRequestDTO } from '../../core/dtos/entry-move-request-dto';
import { Observable, Subscription, switchMap } from 'rxjs';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { NumberedRankingService } from '../../services/numbered-ranking.service';
import { NumberedRankingResolverData } from '../../resolvers/numbered-ranking.resolver';

@Component({
  selector: 'app-numbered-ranking',
  standalone: true,
  imports: [CdkDropList, CdkDrag, ReactiveFormsModule],
  templateUrl: './numbered-ranking.component.html',
  styleUrl: './numbered-ranking.component.css'
})
export class NumberedRankingComponent {

  ranking: NumberedRanking | null = null;
  canEdit: boolean = false;
  userId: number | null = null;

  rankingForm: FormGroup;
  isEditMode: boolean = false;

  private subscriptions = new Subscription();

  readonly TMDB_IMAGE_BASE_URL = 'https://image.tmdb.org/t/p/';
  readonly TMDB_IMAGE_SIZE = 'w92';


  constructor(private route: ActivatedRoute, private router: Router, private fb: FormBuilder, private rankingService: NumberedRankingService) {
    this.rankingForm = this.fb.group({
      title: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(30)]],
      description: ['', [Validators.required, Validators.minLength(1), Validators.maxLength(150)]],
    });
  }

   ngOnInit(): void {
      const data = this.route.snapshot.data['data'] as NumberedRankingResolverData;
      this.ranking = data.ranking as NumberedRanking;
      this.canEdit = data.canEdit;
      this.userId = data.userId;
  
      if (this.ranking) {
        this.rankingForm.patchValue({
          title: this.ranking.title,
          description: this.ranking.description,
        });
      }
    }
  
    toggleEditMode(): void {
      if (!this.canEdit) return; //Guests should not be able to edit
  
      if (this.isEditMode) {
        if (this.rankingForm.valid && this.ranking && this.userId) {
          const updatedRanking = {
            ...this.ranking,
            title: this.rankingForm.value.title,
            description: this.rankingForm.value.description
          };
  
          const updatedRankingSub = this.rankingService.updateRanking(this.userId, this.ranking.id, updatedRanking).subscribe({
            next: () => this.ranking = updatedRanking,
            error: (err) => console.log(err)
          });
          this.subscriptions.add(updatedRankingSub);
        }
      } else if (this.ranking) {
        this.rankingForm.patchValue({
          title: this.ranking.title, 
          description: this.ranking.description
        });
      }
      if (this.rankingForm.valid) {
        this.isEditMode = !this.isEditMode;
      }
    }
  
    drop(event: CdkDragDrop<MediaListEntry[]>): void {
      
      if (!this.isEditMode || !this.ranking?.mediaListDTO?.entries) return;
  
      const entries = this.ranking.mediaListDTO.entries;
      moveItemInArray(entries, event.previousIndex, event.currentIndex);
      this.updateRankings();
  
      const movedEntry = entries[event.currentIndex];
      if (movedEntry && this.userId) {
        const moveRequest: EntryMoveRequestDTO = {
          entryId: movedEntry.id,
          newPosition: movedEntry.ranking,
        };
        this.rankingService.moveEntry(this.userId, this.ranking.id, movedEntry.id, moveRequest).subscribe({
          next: () => console.log('Ranking updated successfully'),
          error: err => console.log(err)
        });
      }
    }
    updateRankings(): void {
      if (!this.ranking?.mediaListDTO?.entries) return;
      this.ranking.mediaListDTO.entries.forEach((entry, index) => entry.ranking = index + 1);
    }
  
    removeEntry(entryId: number): void {
      if (!this.canEdit || !this.ranking || !this.userId) return;
  
      this.rankingService.deleteEntry(this.userId, this.ranking.id, entryId).subscribe({
        next: () => {
          const entries = this.ranking!.mediaListDTO.entries;
          const index = entries.findIndex(entry => entry.id === entryId);
          if (index !== -1) {
            entries.splice(index, 1);
            this.updateRankings();
          }
        },
        error: err => console.log(err)
      });
    }
    goToAddMedia(): void {
      if (this.canEdit && this.ranking && this.userId) {
        this.router.navigate(['/users', this.userId, 'numberedrankings', this.ranking.id, this.ranking.mediaListDTO.entries.length + 1, this.ranking.mediaType]);
      }
    }
  
    isMovieEntry(entry: MediaListEntry): entry is MovieEntry {
      return (entry as MovieEntry).title !== undefined;
    }
  
    isTVShowEntry(entry: MediaListEntry): entry is TVShowEntry {
      return (entry as TVShowEntry).name !== undefined;
    }
  
    ngOnDestroy(): void {
      this.subscriptions.unsubscribe();
    }
  
    get title() { return this.rankingForm.controls['title'];}
    get description() { return this.rankingForm.controls['description'];}
    getPosterUrl(posterPath: string): string {
      return `${this.TMDB_IMAGE_BASE_URL}${this.TMDB_IMAGE_SIZE}${posterPath}`;
    }
  }
