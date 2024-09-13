package com.samcox.ranker.tmdb;

import com.samcox.ranker.media.FilmDTO;
import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.media.MediaListDTO;
import com.samcox.ranker.media.TVShowDTO;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import java.nio.file.AccessDeniedException;

@RestController
public class TmdbController {

  private TmdbService tmdbService;

  public TmdbController(TmdbService tmdbService) {
    this.tmdbService = tmdbService;
  }
  @GetMapping("/tmdb/movies/search")
  public FilmSearchResultListDTO searchMovies(@RequestParam("query") String query) {
    return tmdbService.searchFilms(query);
  }
  @GetMapping("/tmdb/movies/{id}")
  public FilmDTO getMovieDetails(@PathVariable Long id) {
    try {
      return tmdbService.getFilmDetails(id);
    } catch (HttpClientErrorException e) {
      throw new MediaNotFoundException("Movie could not be found with id" + id);
    }
  }
  @GetMapping("/tmdb/tv/search")
  public TVShowSearchResultListDTO searchTVShows(@RequestParam("query") String query) {
    return tmdbService.searchTVShows(query);
  }
  @GetMapping("/tmdb/tv/{id}")
  public TVShowDTO getTVShowDetails(@PathVariable Long id) {
    try {
      return tmdbService.getTVShowDetails(id);
    } catch (HttpClientErrorException e) {
      throw new MediaNotFoundException("TVShow could not be found with id" + id);
    }
  }
}
