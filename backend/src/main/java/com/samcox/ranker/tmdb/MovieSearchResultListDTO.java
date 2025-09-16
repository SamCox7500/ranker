package com.samcox.ranker.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * A list DTO for communicating a list of FilmSearchResultDTOs.
 * <p>Maps to the json response to querying movies by search term in TMDB</p>
 */
public class MovieSearchResultListDTO {
  /**
   * The list of movie search results from a TMDB query
   */
  @JsonProperty("results")
    private List<MovieSearchResultDTO> results;

  /**
   * Returns the list of movie search results
   * @return the list of movie search results
   */
  public List<MovieSearchResultDTO> getResults() {
      return results;
    }
}
