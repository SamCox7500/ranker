package com.samcox.ranker.tmdb;

public class TVShowSearchResultDTO {
  String name;

  String first_air_date;

  String last_air_date;

  String number_of_seasons;

  String number_of_episodes;

  String poster_path;

  public TVShowSearchResultDTO(
    String name,
    String first_air_date,
    String last_air_date,
    String number_of_seasons,
    String number_of_episodes,
    String poster_path
  ) {
    this.name = name;
    this.first_air_date = first_air_date;
    this.last_air_date = last_air_date;
    this.number_of_seasons = number_of_seasons;
    this.number_of_episodes = number_of_episodes;
    this.poster_path = poster_path;
  }
}
