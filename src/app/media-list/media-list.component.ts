import { Component, OnInit } from '@angular/core';
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

@Component({
  selector: 'app-media-list',
  standalone: true,
  imports: [ CdkDropList, CdkDrag ],
  templateUrl: './media-list.component.html',
  styleUrl: './media-list.component.css'
})
export class MediaListComponent implements OnInit {


  //mediaList: MediaList;
  
  rankingId: number | null = null;
  mediaType: string = '';
  mediaListEntries: MediaListEntry[] = [];
  ranking: Ranking | null = null;

  user: User | null = null;

  isEditMode: boolean = false;

  constructor(private route: ActivatedRoute, private mediaListService: MediaListService, private router: Router, private currentUserService: CurrentUserService) {
  }

  toggleEditMode(): void {
    this.isEditMode = !this.isEditMode;
  }

  ngOnInit(): void {
    this.rankingId = Number(this.route.snapshot.paramMap.get('rankingId'));
    this.currentUserService.fetchCurrentUser().subscribe();
    this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
      if (this.user && this.rankingId) {
        this.mediaListService.getMediaList(this.user.id, this.rankingId).subscribe((mediaList: MediaList) => {

          //console.log(mediaList.mediaListEntryDTOList);

          this.mediaType = mediaList.mediaType;
          this.mediaListEntries = mediaList.mediaListEntryDTOList;
          this.ranking = mediaList.numberedRankingDTO;

          this.mediaListEntries.sort((a, b) => a.ranking - b.ranking);
        });
      } else {
        console.log(this.user?.id);
        console.log(this.rankingId)
      }
      //}
      //this.mediaListService.getMediaList(this.user.id, this.rankingId, )

    });
  }

  drop(event: CdkDragDrop<MediaListEntry[]>) {
    if (!this.isEditMode) {
      return;
    }

    moveItemInArray(this.mediaListEntries, event.previousIndex, event.currentIndex);
    this.updateRankings();
    //Update in backend

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
    this.router.navigate(['add-media', this.rankingId, this.mediaListEntries.length+1, this.mediaType]);
  }
  updateRankings(): void {
    this.mediaListEntries.forEach((entry, index) => {
      entry.ranking = index + 1;
    });
  }
}
