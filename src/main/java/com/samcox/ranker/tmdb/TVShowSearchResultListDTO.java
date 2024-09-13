package com.samcox.ranker.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TVShowSearchResultListDTO {

  @JsonProperty("results")
  List<TVShowSearchResultDTO> results;

  public List<TVShowSearchResultDTO> getResults() {
    return results;
  }
}
