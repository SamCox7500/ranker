package com.samcox.ranker.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TMDBSearchResultsDTO {
  @JsonProperty("results")
  private List<TMDBSearchResultDTO> results;
  public List<TMDBSearchResultDTO> getResults() {
    return results;
  }
}
