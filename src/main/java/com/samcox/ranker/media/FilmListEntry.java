package com.samcox.ranker.media;

import jakarta.persistence.Entity;

@Entity
public class FilmListEntry extends MediaListEntry {
  private Long filmId;

  public Long getFilmId() {
    return filmId;
  }
  public void setFilmId(Long filmId) {
    this.filmId = filmId;
  }
}
