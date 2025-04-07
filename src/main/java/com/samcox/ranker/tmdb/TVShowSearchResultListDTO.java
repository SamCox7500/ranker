package com.samcox.ranker.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * Represents a list of TV Show search results from TMDB API
 */
public class TVShowSearchResultListDTO {

  /**
   * List of JSON tv show search results that maps to JSON results property of TMDB API queries
   */
  @JsonProperty("results")
  List<TVShowSearchResultDTO> results;

  /**
   * Returns the JSON list of tv show search results
   * @return the JSON list of tv show search results
   */
  public List<TVShowSearchResultDTO> getResults() {
    return results;
  }
}
