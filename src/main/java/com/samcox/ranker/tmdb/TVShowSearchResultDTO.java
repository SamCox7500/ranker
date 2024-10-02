package com.samcox.ranker.tmdb;

public class TVShowSearchResultDTO extends TMDBSearchResultDTO {
  String name;

  String first_air_date;

  String last_air_date;

  String number_of_seasons;

  String number_of_episodes;

  String poster_path;

  public TVShowSearchResultDTO(
    Long id,
    String name,
    String first_air_date,
    //String last_air_date,
    //String number_of_seasons,
    //String number_of_episodes,
    String poster_path
  ) {
    super(id);
    this.name = name;
    this.first_air_date = first_air_date;
    //this.number_of_seasons = number_of_seasons;
    //this.number_of_episodes = number_of_episodes;
    this.poster_path = poster_path;
  }

  public String getName() {
    return name;
  }

  public String getFirst_air_date() {
    return first_air_date;
  }

  //public String getLast_air_date() {
    //return last_air_date;
  //}

  /*
  public String getNumber_of_seasons() {
    return number_of_seasons;
  }

  public String getNumber_of_episodes() {
    return number_of_episodes;
  }
   */
  public String getPoster_path() {
    return poster_path;
  }
}
