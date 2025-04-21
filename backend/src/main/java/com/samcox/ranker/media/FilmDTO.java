package com.samcox.ranker.media;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO for a film / movie in a MediaListEntry of a MediaList.
 * @see MediaList
 * @see MediaListEntry
 */
public class FilmDTO extends MediaListEntryDTO {

  /**
   * Title of the film in TMDB API.
   */
  private String title;
  /**
   * Release date of the film in TMDB API.
   */
  @JsonProperty("release_date")
  private String releaseDate;

  /**
   * The file path of the poster image of the film.
   */
  private String poster_path;

  /**
   * Default constructor required by JPA.
   */
  public FilmDTO() {

  }

  /**
   * Constructor for FilmDTO.
   * @param id the id of the film in TMDB API
   * @param ranking the number ranking of the film with respect to all other films in the media list.
   * @param title the title of the film
   * @param releaseDate the release date of the film
   * @param poster_path the file path of the poster of the film
   */
  public FilmDTO(Long id, int ranking, String title, String releaseDate, String poster_path) {
    super(id, ranking);
    this.title = title;
    this.releaseDate = releaseDate;
    this.poster_path = poster_path;
  }

  /**
   * Returns the title of the film.
   * @return the title of the film
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the film.
   * @param title the title of the film
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Sets the release date of the film.
   * @param releaseDate the release date of the film
   */
  public void setReleaseDate(String releaseDate) {
    this.releaseDate = releaseDate;
  }

  /**
   * Returns the release date of the film.
   * @return the release date of the film
   */
  public String getReleaseDate() {
    return releaseDate;
  }

  /**
   * Returns the poster path of the film.
   * @return the poster path of the film
   */
  public String getPoster_path() {
    return poster_path;
  }

  /**
   * Sets the poster path of the film.
   * @param poster_path the file path for the poster of the film
   */
  public void setPoster_path(String poster_path) {
    this.poster_path = poster_path;
  }

  /**
   * Returns the FilmDTO as a string.
   * @return the string representation of the FilmDTO
   */
  @Override
  public String toString() {
    return "FilmDTO{" +
      "title='" + title + '\'' +
      ", releaseDate='" + releaseDate + '\'' +
      '}';
  }
}
