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

  constructor(private route: ActivatedRoute, private router: Router, private tmdbService: TMDBService, private mediaListService: MediaListService) { }

  ngOnInit(): void {
    this.rankingId = Number(this.route.snapshot.paramMap.get('rankingId'));
    this.mediaType = this.route.snapshot.paramMap.get('mediaType') || '';
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
}
