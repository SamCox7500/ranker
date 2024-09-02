package com.samcox.ranker.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class TMDBFilmSearchResultDTO {
  @JsonProperty("results")
  private List<TMDBFilmSearchResultDTO> results;
}
