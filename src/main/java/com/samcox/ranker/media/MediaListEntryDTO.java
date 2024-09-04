package com.samcox.ranker.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MediaListEntryDTO {
  private Long id;
  private int ranking;
  private MediaDTO mediaDTO;

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

  public void setMediaDTO(MediaDTO mediaDTO) {
    this.mediaDTO = mediaDTO;
  }
  public MediaDTO getMediaDTO() {
    return mediaDTO;
  }
}
