package com.samcox.ranker.tmdb;

/**
 * DTO for a single entry in a search querying TheMovieDatabase API.
 *
 * <p>Consists of a TMDB ID which represents a film or movie id in TMDB API</p>
 */
public class TMDBSearchResultDTO {

  /**
   * The TMDB ID of the search result entry.
   */
  private Long id;

  /**
   * The default constructor as required by JPA.
   */
  public TMDBSearchResultDTO() {

  }

  /**
   * Constructor for creating DTO with specified ID
   * @param id the TMDB id of the search result the DTO represents
   */
  public TMDBSearchResultDTO(Long id) {
    this.id = id;
  }

  /**
   * Returns the id of the search result
   * @return the id of the search result
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets a new id for the search result
   * @param id the new id for the search result
   */
  public void setId(Long id) {
    this.id = id;
  }
}
