package com.samcox.ranker.media;

import jakarta.validation.constraints.NotNull;

public class EntryAddRequest {
  @NotNull
  private Long tmdbId;
  private int ranking;

  public Long getTmdbId() {
    return tmdbId;
  }
  public void setTmdbId(Long tmdbId) {
    this.tmdbId = tmdbId;
  }

  public int getRanking() {
    return ranking;
  }
  public void setRanking(int ranking) {
    this.ranking = ranking;
  }
}
