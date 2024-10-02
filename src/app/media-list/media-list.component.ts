import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { MediaListService } from '../services/media-list.service';
import { MediaList } from '../media-list';
import { CdkDragDrop, moveItemInArray } from '@angular/cdk/drag-drop'
import { MediaListEntry } from '../media-list-entry';
import { Ranking } from '../ranking';
import { User } from '../user';
import { CurrentUserService } from '../services/current-user.service';
import { MovieEntry } from '../movie-entry';
import { TVShowEntry } from '../tvshow-entry';

@Component({
  selector: 'app-media-list',
  standalone: true,
  imports: [],
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
          this.mediaType = mediaList.mediaType;
          this.mediaListEntries = mediaList.mediaListEntryDTOList;
          this.ranking = mediaList.numberedRankingDTO;
        });
      } else {
        console.log(this.user?.id);
        console.log(this.rankingId)
      }
      //}
      //this.mediaListService.getMediaList(this.user.id, this.rankingId, )

    });
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
}
