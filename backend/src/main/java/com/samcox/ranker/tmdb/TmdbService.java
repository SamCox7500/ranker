package com.samcox.ranker.tmdb;

import com.samcox.ranker.media.FilmDTO;
import com.samcox.ranker.media.TVShowDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * The service for interacting with the TMDB API.
 * <p>Includes searching for movies and tv shows, as well as getting details on specific movies and tv shows by ID</p>
 */
@Service
public class TmdbService {

  /**
   * RestTemplate for http requests to TMDB API
   */
  private final RestTemplate restTemplate;
  /**
   * The apiKey for accessing TMDB API
   */
  @Value("${tmdb.api.key}")
  private String apiKey;

  /**
   * The url for accessing the TMDB API
   */
  @Value("${tmdb.api.url:https://api.themoviedb.org/3}")
  public String tmdbApiURL;

  /**
   * Constructor for TMDBService. Assigns RestTemplate for get requests.
   * @param restTemplate RestTemplate for requests to TMDB API
   */
  public TmdbService(RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
  }

  /**
   * Returns film dto representing a film in TMDB API
   * @param tmdbId the id of the film in the TMDB API
   * @return the film dto of the TMDB film
   */
  public FilmDTO getFilmDetails(Long tmdbId) {
    URI uri = UriComponentsBuilder.fromHttpUrl(tmdbApiURL)
      .pathSegment("movie", tmdbId.toString())
      .queryParam("api_key", apiKey)
      .build()
      .toUri();

    return restTemplate.getForObject(uri, FilmDTO.class);
  }

  /**
   * Returns the DTO of a JSON list of search results from querying TMDB API
   * @param query the search term used for querying TMDB API
   * @return the DTO of movie search results
   */
  public FilmSearchResultListDTO searchFilms(String query) {
    URI uri = UriComponentsBuilder.fromHttpUrl(tmdbApiURL)
      .pathSegment("search", "movie")
      .queryParam("api_key", apiKey)
      .queryParam("query", query)
      .build()
      .toUri();

    return restTemplate.getForObject(uri, FilmSearchResultListDTO.class);
  }

  /**
   * Returns tv show DTO representing a tv show in TMDB API
   * @param tmdbId the id of the tv show in the TMDB API
   * @return the tv show dto of the TMDB tv show
   */
  public TVShowDTO getTVShowDetails(Long tmdbId) {
    URI uri = UriComponentsBuilder.fromHttpUrl(tmdbApiURL)
      .pathSegment("tv", tmdbId.toString())
      .queryParam("api_key", apiKey)
      .build()
      .toUri();

    return restTemplate.getForObject(uri, TVShowDTO.class);
  }

  /**
   * Returns the DTO of a JSON list of search results from querying TMDB API
   * @param query the search term used for querying TMDB API
   * @return the DTO of tv show search results
   */
  public TVShowSearchResultListDTO searchTVShows(String query) {
    URI uri = UriComponentsBuilder.fromHttpUrl(tmdbApiURL)
      .pathSegment("search", "tv")
      .queryParam("api_key", apiKey)
      .queryParam("query", query)
      .build()
      .toUri();

    return restTemplate.getForObject(uri, TVShowSearchResultListDTO.class);
  }
}
