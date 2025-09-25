import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MovieDetails } from '../core/dtos/movie-details';
import { TVShowDetails } from '../core/dtos/tvshow-details';
import { MovieSearchResult } from '../core/dtos/movie-search-result';
import { MovieSearchResultList } from '../core/dtos/movie-search-result-list';
import { TVShowSearchResultList } from '../core/dtos/tvshow-search-result-list';
import { MediaSearchResult } from '../core/dtos/media-search-result';
import { MediaSearchResultList } from '../core/dtos/media-search-result-list';
import { environment } from '../environment';

@Injectable({
  providedIn: 'root'
})
export class TMDBService {

  private movieURL: string = `${environment.apiUrl}/tmdb/movies`;
  private tvShowURL: string = `${environment.apiUrl}/tmdb/tv`;

  constructor(private http: HttpClient) {}

  public getMovieDetails(movieID: number) : Observable<MovieDetails> {
    return this.http.get<MovieDetails>(`${this.movieURL}/${movieID}`, {withCredentials: true});
  }
  public getTVShowDetails(tvShowID: number) : Observable<TVShowDetails> {
    return this.http.get<TVShowDetails>(`${this.tvShowURL}/${tvShowID}`, {withCredentials: true});
  }
  public searchMovies(query: string) : Observable<MediaSearchResultList> {
    return this.http.get<MediaSearchResultList>(`${this.movieURL}/search`, {params: { query: query }, withCredentials : true});
  }
  public searchTVShows(query: string) : Observable<MediaSearchResultList> {
    return this.http.get<MediaSearchResultList>(`${this.tvShowURL}/search`, {params: { query: query }, withCredentials : true});
  }
}
