package com.samcox.ranker.tmdb;

import com.samcox.ranker.media.MediaDTO;

public class TMDBSearchResultDTO {
  private Long id;

  public TMDBSearchResultDTO(Long id) {

  }
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
}
