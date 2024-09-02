package com.samcox.ranker.media;

public class AddMediaRequest {
  private Long tmdbId;
  private MediaType mediaType;

  public Long getTmdbId() {
    return tmdbId;
  }
  public void setTmdbId(Long tmdbId) {
    this.tmdbId = tmdbId;
  }
  public MediaType getMediaType() {
    return mediaType;
  }
  public void setMediaType(MediaType mediaType) {
    this.mediaType = mediaType;
  }
}
