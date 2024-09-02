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
