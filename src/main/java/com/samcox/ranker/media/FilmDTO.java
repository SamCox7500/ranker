package com.samcox.ranker.media;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilmDTO extends MediaDTO {

  @JsonProperty("release_date")
  private String releaseDate;

  public FilmDTO(String title, String releaseDate) {
    super(title);
    this.releaseDate = releaseDate;
  }
  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }
  public String getReleaseDate() {
    return releaseDate;
  }

  @Override
  public String toString() {
    return "FilmDTO{" +
      "releaseDate='" + releaseDate + '\'' +
      '}';
  }
}
