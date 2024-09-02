package com.samcox.ranker.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

public class MediaListEntryDTO {
  private Long id;
  private Long tmdbId;
  private int ranking;
  private String title;
  private MediaType mediaType;

  public MediaListEntryDTO(Long id, Long tmdbId, int ranking, String title, MediaType mediaType) {
    this.id = id;
    this.tmdbId = tmdbId;
    this.ranking = ranking;
    this.title = title;
    this.mediaType = mediaType;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
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
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public MediaType getMediaType() {
    return mediaType;
  }
  public void setMediaType(MediaType mediaType) {
    this.mediaType = mediaType;
  }
}
