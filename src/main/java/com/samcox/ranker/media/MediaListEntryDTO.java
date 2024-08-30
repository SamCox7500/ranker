package com.samcox.ranker.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public class MediaListEntryDTO {
  private Long id;
  @NotNull
  private int ranking;

  @NotNull
  private MediaList mediaList;

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

  public MediaList getMediaList() {
    return mediaList;
  }
  public void setMediaList(MediaList mediaList) {
    this.mediaList = mediaList;
  }
}
