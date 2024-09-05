package com.samcox.ranker.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FilmSearchResultListDTO {
    @JsonProperty("results")
    private List<FilmSearchResultDTO> results;
    public List<FilmSearchResultDTO> getResults() {
      return results;
    }
}
