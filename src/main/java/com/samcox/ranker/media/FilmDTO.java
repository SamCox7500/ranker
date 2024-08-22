package com.samcox.ranker.media;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FilmDTO {
  private Long id;
  @NotBlank
  private Long tmdbFilmId;

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public Long getTmdbFilmId() {
    return tmdbFilmId;
  }
  public void setTmdbFilmId(Long tmdbFilmId) {
    this.tmdbFilmId = tmdbFilmId;
  }
}
