package com.samcox.ranker.tmdb;

import com.samcox.ranker.media.FilmDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Service
public class TmdbService {

  private final RestTemplate restTemplate;
  @Value("${tmdb.api.key}")
  private String apiKey;

  @Value("${tmdb.api.url:https://api.themoviedb.org/3}")
  public String tmdbApiURL;

  public TmdbService(RestTemplate restTemplate) {
      this.restTemplate = restTemplate;
  }
  public TMDBFilmSearchResultsDTO searchFilms(String query) {
    URI uri = UriComponentsBuilder.fromHttpUrl(tmdbApiURL)
      .pathSegment("search", "movie")
      .queryParam("api_key", apiKey)
      .queryParam("query", query)
      .build()
      .toUri();
    return restTemplate.getForObject(uri, TMDBFilmSearchResultsDTO.class);
  }
  public FilmDTO getFilmDetails(Long tmdbId) {
    URI uri = UriComponentsBuilder.fromHttpUrl(tmdbApiURL)
      .pathSegment("movie", tmdbId.toString())
      .queryParam("api_key", apiKey)
      .build()
      .toUri();

    return restTemplate.getForObject(uri, FilmDTO.class);
  }
  //todo tv show details
}
