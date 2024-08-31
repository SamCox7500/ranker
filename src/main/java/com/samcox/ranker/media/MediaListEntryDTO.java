package com.samcox.ranker.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public class MediaListEntryDTO {
  private Long id;
  @NotNull
  private int ranking;

  private String tmdbId;

  public MediaListEntryDTO(Long id, int ranking, String tmdbId) {

  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public int getRanking() {
    return ranking;
  }

  public void setRanking(int ranking) {
    this.ranking = ranking;
  }

  public String getTmdbId() {
    return tmdbId;
  }
  public void setTmdbId(String tmdbId) {
    this.tmdbId = tmdbId;
  }
}
