package com.samcox.ranker.media;

public class TVShowDTO extends MediaListEntryDTO {
  String name;

  String first_air_date;

  String last_air_date;

  String number_of_seasons;

  String number_of_episodes;

  String poster_path;

  public TVShowDTO(
    Long id,
    int ranking,
    String name,
    String first_air_date,
    String last_air_date,
    String number_of_seasons,
    String number_of_episodes,
    String poster_path
  ) {
    super(id, ranking);
    this.name = name;
    this.first_air_date = first_air_date;
    this.last_air_date = last_air_date;
    this.number_of_seasons = number_of_seasons;
    this.number_of_episodes = number_of_episodes;
    this.poster_path = poster_path;
  }
  public String getName() {
    return name;
  }
  public void setName(String name) {
    this.name = name;
  }
  public String getFirst_air_date() {
    return first_air_date;
  }
  public void setFirst_air_date(String first_air_date) {
    this.first_air_date = first_air_date;
  }
  public String getLast_air_date() {
    return last_air_date;
  }
  public void setLast_air_date(String last_air_date) {
    this.last_air_date = last_air_date;
  }
  public String getNumber_of_seasons() {
    return number_of_seasons;
  }
  public void setNumber_of_seasons(String number_of_seasons) {
    this.number_of_seasons = number_of_seasons;
  }
  public String getNumber_of_episodes() {
    return number_of_episodes;
  }
  public void setNumber_of_episodes(String number_of_episodes) {
    this.number_of_episodes = number_of_episodes;
  }
  public String getPoster_path() {
    return poster_path;
  }
  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }
}
