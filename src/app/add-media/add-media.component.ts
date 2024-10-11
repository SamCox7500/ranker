import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { TMDBService } from '../services/tmdb.service';
import { MediaListService } from '../services/media-list.service';
import { FormControl, ReactiveFormsModule } from '@angular/forms';
import { MovieSearchResult } from '../movie-search-result';
import { TVShowSearchResult } from '../tvshow-search-result';
import { TVShowSearchResultList } from '../tvshow-search-result-list';
import { MovieSearchResultList } from '../movie-search-result-list';
import { MediaSearchResult } from '../media-search-result';
import { MediaSearchResultList } from '../media-search-result-list';
import { CurrentUserService } from '../services/current-user.service';
import { User } from '../user';
import { EntryAddRequestDTO } from '../entry-add-request-dto';

@Component({
  selector: 'app-add-media',
  standalone: true,
  imports: [ ReactiveFormsModule ],
  templateUrl: './add-media.component.html',
  styleUrl: './add-media.component.css'
})
export class AddMediaComponent implements OnInit {

  rankingId: number | null = null;
  mediaType: string = '';

  query = new FormControl('');
  mediaResults: MediaSearchResult[] = [];

  user: User | null = null;
  addMediaRanking: number | null = null;

  readonly TMDB_IMAGE_BASE_URL = 'https://image.tmdb.org/t/p/';
  readonly TMDB_IMAGE_SIZE = 'w92';

  constructor(private route: ActivatedRoute, private router: Router, private tmdbService: TMDBService, private mediaListService: MediaListService, private currentUserService: CurrentUserService) { }

  ngOnInit(): void {
    this.rankingId = Number(this.route.snapshot.paramMap.get('rankingId'));
    this.mediaType = this.route.snapshot.paramMap.get('mediaType') || '';
    this.addMediaRanking = Number(this.route.snapshot.paramMap.get('addMediaRanking'));
    this.currentUserService.fetchCurrentUser().subscribe();
    this.currentUserService.getCurrentUser().subscribe((user: User | null) => {
      this.user = user;
    });
  }
  onSearch(): void {
    if(this.mediaType == 'FILM') {
      this.tmdbService.searchMovies(this.query.value || '').subscribe((mediaSearchResultList: MediaSearchResultList) => {
        this.mediaResults = mediaSearchResultList.results;
      });
    } else if(this.mediaType == 'TV_SHOW') {
      this.tmdbService.searchTVShows(this.query.value || '').subscribe((mediaSearchResultList: MediaSearchResultList) => {
        this.mediaResults = mediaSearchResultList.results;
      });
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
      const entryAddRequest: EntryAddRequestDTO = {tmdbId: mediaId, ranking: this.addMediaRanking};
      //console.log('User Id: ' + this.user.id);
      //console.log('Ranking Id: ' + this.rankingId);
      //console.log('Ranking: ' + entryAddRequest.ranking);
      //console.log('TmdbId: ' + entryAddRequest.tmdbId);
      this.mediaListService.addEntry(this.user.id, this.rankingId, entryAddRequest).subscribe({
        next: () => this.goToMediaList(),
        error: err => console.log('Error trying to add media to ranking'),
      });
    } else {
      console.log('Unable to add new entry to ranking list. Invalid ');
    }
  }
  goToMediaList() {
    this.router.navigate(['/medialist', this.rankingId]);
  }
  getPosterUrl(posterPath: string): string {
    return `${this.TMDB_IMAGE_BASE_URL}${this.TMDB_IMAGE_SIZE}${posterPath}`;
  }
}
