package com.samcox.ranker.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

public class MediaListEntryDTO {
  private Long id;
  private int ranking;

  public MediaListEntryDTO() {

  }
  public MediaListEntryDTO(Long id, int ranking) {
    this.id = id;
    this.ranking = ranking;
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

}
