package com.samcox.ranker.tmdb;

public class FilmSearchResultDTO extends TMDBSearchResultDTO {

  String title;
  String release_date;
  String poster_path;

  public FilmSearchResultDTO() {
      super();
  }
  public FilmSearchResultDTO(Long id, String title, String release_date, String poster_path) {
    super(id);
    this.title = title;
    this.release_date = release_date;
    this.poster_path = poster_path;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getRelease_date() {
    return release_date;
  }
  public void setRelease_date(String release_date) {
    this.release_date = release_date;
  }
  public String getPoster_path() {
    return poster_path;
  }
  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }
  @Override
  public String toString() {
    return "FilmSearchResultDTO{" +
      "title='" + title + '\'' +
      ", release_date='" + release_date + '\'' +
      ", poster_path='" + poster_path + '\'' +
      '}';
  }
}
