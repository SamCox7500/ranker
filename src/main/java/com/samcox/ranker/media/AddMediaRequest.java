package com.samcox.ranker.media;

public class AddMediaRequest {
  private int tmdbId;
  private MediaType mediaType;
  private int ranking;

  public Long getTmdbId() {
    return tmdbId;
  }
  public void setTmdbId(int tmdbId, MediaType mediaType, int ranking) {
    this.tmdbId = tmdbId;
    this.mediaType = mediaType;
    this.ranking = ranking;
  }
  public MediaType getMediaType() {
    return mediaType;
  }
  public void setMediaType(MediaType mediaType) {
    this.mediaType = mediaType;
  }

  public int getRanking() {
    return ranking;
  }
  public void setRanking(int ranking) {
    this.ranking = ranking;
  }
}
