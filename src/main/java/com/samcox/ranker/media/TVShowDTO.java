package com.samcox.ranker.media;

/**
 * DTO for a TV Show in TMDB API.
 * Each JSON attribute in TMDB API maps to an attribute in this class.
 * @see MediaListEntry
 */
public class TVShowDTO extends MediaListEntryDTO {
  /**
   * The name of the TV Show.
   */
  String name;

  /**
   * The earliest date the tv show aired.
   */
  String first_air_date;

  /**
   * The date that the last episode of the tv show aired.
   */
  String last_air_date;

  /**
   * The number of seasons that the tv show has.
   */
  String number_of_seasons;

  /**
   * The number of episodes that the tv show has.
   */
  String number_of_episodes;

  /**
   * The file path for the poster image of the tv show.
   */
  String poster_path;

  /**
   * The constructor for TVShowDTO
   * @param id the id of the {@link MediaListEntry} that represents the tv show
   * @param ranking the ranking of the tv show in the list that contains it
   * @param name the name of the tv show
   * @param first_air_date the earliest date the tv show aired
   * @param last_air_date the date that the last episode of the tv show aired
   * @param number_of_seasons the number of seasons that the tv show has
   * @param number_of_episodes the number of episodes that the tv show has
   * @param poster_path the file path for the number of episodes that the tv show has
   */
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

  /**
   * Returns the name of the tv show
   * @return the name of the tv show
   */
  public String getName() {
    return name;
  }

  /**
   * Sets the name of the tv show
   * @param name the name of the tv show
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Returns the first air date of the tv show
   * @return the first air date of the tv show
   */
  public String getFirst_air_date() {
    return first_air_date;
  }

  /**
   * Sets the first air date of the tv show
   * @param first_air_date the first air date of the tv show
   */
  public void setFirst_air_date(String first_air_date) {
    this.first_air_date = first_air_date;
  }

  /**
   * Returns the last air date of the tv show
   * @return the last air date of the tv show
   */
  public String getLast_air_date() {
    return last_air_date;
  }

  /**
   * Sets the last air date of the tv show
   * @param last_air_date the last air date of the tv show
   */
  public void setLast_air_date(String last_air_date) {
    this.last_air_date = last_air_date;
  }

  /**
   * Returns the number of seasons of the tv show
   * @return the number of seasons of the tv show
   */
  public String getNumber_of_seasons() {
    return number_of_seasons;
  }

  /**
   * Sets the number of seasons of the tv show
   * @param number_of_seasons the number of seasons of the tv show
   */
  public void setNumber_of_seasons(String number_of_seasons) {
    this.number_of_seasons = number_of_seasons;
  }

  /**
   * Returns the number of episodes of the tv show
   * @return the number of episodes of the tv show
   */
  public String getNumber_of_episodes() {
    return number_of_episodes;
  }

  /**
   * Sets the number of episodes of the tv show
   * @param number_of_episodes the number of episodes of the tv show
   */
  public void setNumber_of_episodes(String number_of_episodes) {
    this.number_of_episodes = number_of_episodes;
  }

  /**
   * Returns the poster path of the tv show
   * @return the poster path of the tv show
   */
  public String getPoster_path() {
    return poster_path;
  }

  /**
   * Sets the poster path of the tv show
   * @param poster_path the poster path of the tv show
   */
  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }
}
