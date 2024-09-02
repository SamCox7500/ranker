package com.samcox.ranker.media;

public class MediaDetailsDTO {
  private Long tmdbid;
  private String title;
  private MediaType mediaType;

  public Long getTmdbid() {
    return tmdbid;
  }

  public void setTmdbid(Long tmdbid) {
    this.tmdbid = tmdbid;
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
