package com.samcox.ranker.tmdb;

/**
 * Represents a single JSON Movie query result from TMDB
 *
 * @see TMDBSearchResultDTO
 */
public class FilmSearchResultDTO extends TMDBSearchResultDTO {

  /**
   * The title of the movie
   */
  String title;
  /**
   * The release date of the movie
   */
  String release_date;
  /**
   * The url for the image of the movie poster
   */
  String poster_path;

  /**
   * Default constructor as required by JPA
   */
  public FilmSearchResultDTO() {
      super();
  }

  /**
   * Constructor for creating movie search result.
   * Includes parent call to {@link TMDBSearchResultDTO} constructor, for assigning movie ID.
   *
   * @param id the id of the movie in TMDB API
   * @param title the title of the movie
   * @param release_date the release date of the movie
   * @param poster_path the poster path of the movie
   */
  public FilmSearchResultDTO(Long id, String title, String release_date, String poster_path) {
    super(id);
    this.title = title;
    this.release_date = release_date;
    this.poster_path = poster_path;
  }

  /**
   * Returns the title of the movie
   * @return the title of the movie
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets a new title for the movie
   * @param title the new title for the movie
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the release date of the movie
   * @return the release date of the movie
   */
  public String getRelease_date() {
    return release_date;
  }

  /**
   * Sets a new release date for the movie
   * @param release_date the new release date for the movie
   */
  public void setRelease_date(String release_date) {
    this.release_date = release_date;
  }

  /**
   * Retyrns the poster path of the movie
   * @return the url for the poster image of the movie
   */
  public String getPoster_path() {
    return poster_path;
  }

  /**
   * Sets the poster path of the movie
   * @param poster_path the poster path of the movie
   */
  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }

  /**
   * Returns the movie search result as a string
   * @return the string representation of the movie search result
   */
  @Override
  public String toString() {
    return "FilmSearchResultDTO{" +
      "title='" + title + '\'' +
      ", release_date='" + release_date + '\'' +
      ", poster_path='" + poster_path + '\'' +
      '}';
  }
}
