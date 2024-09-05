package com.samcox.ranker.media;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilmDTO extends MediaListEntryDTO {

  private String title;
  @JsonProperty("release_date")
  private String releaseDate;

  public FilmDTO(Long id, int ranking, String title, String releaseDate) {
    super(id, ranking);
    this.title = title;
    this.releaseDate = releaseDate;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
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
      "title='" + title + '\'' +
      ", releaseDate='" + releaseDate + '\'' +
      '}';
  }
}
