package com.samcox.ranker.media;

import com.fasterxml.jackson.annotation.JsonProperty;

public class FilmDTO extends MediaListEntryDTO {

  private String title;
  @JsonProperty("release_date")
  private String releaseDate;

  private String poster_path;

  public FilmDTO() {

  }
  public FilmDTO(Long id, int ranking, String title, String releaseDate, String poster_path) {
    super(id, ranking);
    this.title = title;
    this.releaseDate = releaseDate;
    this.poster_path = poster_path;
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
  public String getPoster_path() {
    return poster_path;
  }
  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }
  @Override
  public String toString() {
    return "FilmDTO{" +
      "title='" + title + '\'' +
      ", releaseDate='" + releaseDate + '\'' +
      '}';
  }
}
