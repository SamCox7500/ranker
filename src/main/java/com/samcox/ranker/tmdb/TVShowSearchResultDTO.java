package com.samcox.ranker.tmdb;

/**
 * Represents a single result for a tv show in a search query to TMDB
 *
 * @see TMDBSearchResultDTO
 */
public class TVShowSearchResultDTO extends TMDBSearchResultDTO {
  /**
   * The name of the tv show
   */
  String name;

  /**
   * the earliest date the tv show aired
   */
  String first_air_date;

  /**
   * the latest date the tv show aired
   */
  String last_air_date;

  /**
   * the number of seasons of the tv show
   */
  String number_of_seasons;

  /**
   * the number of episodes of the tv show
   */
  String number_of_episodes;

  /**
   * the poster image path of the tv show
   */
  String poster_path;

  /**
   * Constructor for the tv show search result DTO
   * @param id the id of the tv show in TMDB API
   * @param name the name of the tv show
   * @param first_air_date the first air date of the tv show
   * @param poster_path the poster path of the tv show
   */
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

  /**
   * Returns the name of the tv show
   * @return the name of the tv show
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the earliest air date of the tv show
   * @return the first air date of the tv show
   */
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

  /**
   * Returns the url for the tv show poster
   * @return the poster path of the tv show
   */
  public String getPoster_path() {
    return poster_path;
  }
}
