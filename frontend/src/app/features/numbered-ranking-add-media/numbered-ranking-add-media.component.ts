import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TMDBService } from '../../services/tmdb.service';
import { MediaListService } from '../../services/media-list.service';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MovieSearchResult } from '../../core/dtos/movie-search-result';
import { TVShowSearchResult } from '../../core/dtos/tvshow-search-result';
import { TVShowSearchResultList } from '../../core/dtos/tvshow-search-result-list';
import { MovieSearchResultList } from '../../core/dtos/movie-search-result-list';
import { MediaSearchResult } from '../../core/dtos/media-search-result';
import { MediaSearchResultList } from '../../core/dtos/media-search-result-list';
import { CurrentUserService } from '../../services/current-user.service';
import { User } from '../../core/models/user';
import { EntryAddRequestDTO } from '../../core/dtos/entry-add-request-dto';
import { Subscription } from 'rxjs';
import { MediaList } from '../../core/models/media-list';
import { NumberedRankingService } from '../../services/numbered-ranking.service';

@Component({
  selector: 'app-numbered-ranking-add-media',
  standalone: true,
  imports: [ ReactiveFormsModule ],
  templateUrl: './numbered-ranking-add-media.component.html',
  styleUrl: './numbered-ranking-add-media.component.css'
})
export class NumberedRankingAddMediaComponent {
  rankingId: number | null = null;
  mediaType: string = '';
  rankingType: string = '';

  query = new FormControl('');
  mediaResults: MediaSearchResult[] = [];
  //existingMediaIds = new Set<number>();

  user: User | null = null;
  addMediaRanking: number | null = null;

  readonly TMDB_IMAGE_BASE_URL = 'https://image.tmdb.org/t/p/';
  readonly TMDB_IMAGE_SIZE = 'w92';

  private subscriptions : Subscription = new Subscription();

  constructor(private route: ActivatedRoute, private router: Router, private tmdbService: TMDBService, private currentUserService: CurrentUserService, private numberedRankingService: NumberedRankingService) { }

  ngOnInit(): void {
    this.rankingId = Number(this.route.snapshot.paramMap.get('rankingId'));
    this.mediaType = this.route.snapshot.paramMap.get('mediaType') || '';
    this.addMediaRanking = Number(this.route.snapshot.paramMap.get('addMediaRanking'));
    const fetchUserSub = this.currentUserService.fetchCurrentUser().subscribe();
    const currentUserSub = this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
    });
    this.subscriptions.add(fetchUserSub);
    this.subscriptions.add(currentUserSub);
  }
  onSearch(): void {
    if (this.mediaType === 'FILM') {
      const movieSub = this.tmdbService.searchMovies(this.query.value || '').subscribe((mediaSearchResultList: MediaSearchResultList) => {
        this.mediaResults = mediaSearchResultList.results;
      });
      this.subscriptions.add(movieSub);
    } else if (this.mediaType === 'TV_SHOW') {
      const tvShowSub = this.tmdbService.searchTVShows(this.query.value || '').subscribe((mediaSearchResultList: MediaSearchResultList) => {
        this.mediaResults = mediaSearchResultList.results;
      });
      this.subscriptions.add(tvShowSub);
    }
  }
  isMovieSearchResult(result: MediaSearchResult): result is MovieSearchResult {
    return (result as MovieSearchResult).title !== undefined;
  }
  isTVShowSearchResult(result: MediaSearchResult): result is TVShowSearchResult {
    return (result as TVShowSearchResult).name !== undefined;
  }
  addMediaToRanking(mediaId: number): void {
    if (this.addMediaRanking && this.user && this.rankingId) {
      const entryAddRequest: EntryAddRequestDTO = { tmdbId: mediaId, ranking: this.addMediaRanking };
      const addMediaSub = this.numberedRankingService.addEntry(this.user.id, this.rankingId, entryAddRequest).subscribe({
        next: () => this.goToNumberedRanking(),
        error: err => console.log('Error trying to add media to numbered ranking'),

      });
      this.subscriptions.add(addMediaSub);
    } else {
      console.log('Unable to add new entry to ranking list. Invalid parameters');
    }
  }
  goToNumberedRanking() {
    this.router.navigate(['/numberedrankings', this.rankingId]);
  }
  getPosterUrl(posterPath: string): string {
    return `${this.TMDB_IMAGE_BASE_URL}${this.TMDB_IMAGE_SIZE}${posterPath}`;
  }
  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }
  /*
  loadExistingMediaList(): void {
    if (this.user && this.rankingId) {
      const mediaListSub = this.mediaListService.getMediaList(this.user.id, this.rankingId).subscribe((mediaList: MediaList) => {
        console.log(mediaList);
        this.existingMediaIds = new Set(mediaList.mediaListEntryDTOList.map(mediaListEntry => mediaListEntry.id));
      });
      this.subscriptions.add(mediaListSub);
    }
  }
  filterResults(results: MediaSearchResult[]): void {
    this.mediaResults = results.filter(result => !this.existingMediaIds.has(result.id));
  }
  */
}
