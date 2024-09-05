package com.samcox.ranker.tmdb;

public class FilmSearchResultDTO extends TMDBSearchResultDTO {

  String title;
  String release_date;
  String poster_path;

  public FilmSearchResultDTO(Long id, String title, String release_date, String poster_path) {
    super(id);
    this.title = title;
    this.release_date = release_date;
    this.poster_path = poster_path;
  }
}
