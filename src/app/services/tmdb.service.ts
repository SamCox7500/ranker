import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { MovieDetails } from '../movie-details';
import { TVShowDetails } from '../tvshow-details';
import { MovieSearchResult } from '../movie-search-result';
import { MovieSearchResultList } from '../movie-search-result-list';
import { TVShowSearchResultList } from '../tvshow-search-result-list';
import { MediaSearchResult } from '../media-search-result';
import { MediaSearchResultList } from '../media-search-result-list';


@Injectable({
  providedIn: 'root'
})
export class TMDBService {

  private movieURL: string = 'http://localhost:8080/tmdb/movies';
  private tvShowURL: string = 'http://localhost:8080/tmdb/tv';

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
