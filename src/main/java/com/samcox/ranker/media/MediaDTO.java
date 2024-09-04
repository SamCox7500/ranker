package com.samcox.ranker.media;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.TileObserver;

public class MediaDTO {

  @JsonProperty("title")
  private String title;

  public MediaDTO(String title) {
    this.title = title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
  public String getTitle() {
    return title;
  }
  @Override
  public String toString() {
    return "MediaDTO{" +
      "title='" + title + '\'' +
      '}';
  }
}
