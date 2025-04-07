package com.samcox.ranker.tmdb;

import com.samcox.ranker.media.FilmDTO;
import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.media.MediaListDTO;
import com.samcox.ranker.media.TVShowDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.AccessDeniedException;

/**
 * REST controller for requests to search for movies and tv shows,
 * as well as access data on specific tv shows and movies.
 */
@RestController
public class TmdbController {

  /**
   * The TMDBService used for interacting with TMDB API for movie and tv show data
   */
  private TmdbService tmdbService;

  /**
   * Constructor for the TMDBController. Assigns TMDBService for communicating with TMDB API
   * @param tmdbService the tmdb service for api calls
   */
  public TmdbController(TmdbService tmdbService) {
    this.tmdbService = tmdbService;
  }

  /**
   * Returns a list of search results for films queried in TMDB API
   * @param query the search term used to query TMDB API
   * @return the list of films from the resulting query
   */
  @GetMapping("/tmdb/movies/search")
  public FilmSearchResultListDTO searchMovies(@RequestParam("query") String query) {
    return tmdbService.searchFilms(query);
  }

  /**
   * Returns a FilmDTO of a film in TMDB API
   * @param id the TMDB id of the film to be accessed
   * @return film dto with details on the movie
   */
  @GetMapping("/tmdb/movies/{id}")
  public FilmDTO getMovieDetails(@PathVariable Long id) {
    try {
      return tmdbService.getFilmDetails(id);
    } catch (HttpClientErrorException e) {
      throw new MediaNotFoundException("Movie could not be found with id" + id);
    }
  }

  /**
   * Returns a list of tv show search results for tv shows queried in TMDB API
   * @param query the search term used to query TMDB API
   * @return the list of tv shows as a DTO
   */
  @GetMapping("/tmdb/tv/search")
  public TVShowSearchResultListDTO searchTVShows(@RequestParam("query") String query) {
    return tmdbService.searchTVShows(query);
  }

  /**
   * Returns a tv show DTO with details on a tv show in TMDB API
   * @param id the id of the tv show in the TMDB API
   * @return the TVShowDTO containing the tv show details of the specified tv show
   */
  @GetMapping("/tmdb/tv/{id}")
  public TVShowDTO getTVShowDetails(@PathVariable Long id) {
    try {
      return tmdbService.getTVShowDetails(id);
    } catch (HttpClientErrorException e) {
      throw new MediaNotFoundException("TVShow could not be found with id" + id);
    }
  }
}
