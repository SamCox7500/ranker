package com.samcox.ranker.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public class MediaListEntryDTO {
  private Long id;
  @NotNull
  private int ranking;

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
}
