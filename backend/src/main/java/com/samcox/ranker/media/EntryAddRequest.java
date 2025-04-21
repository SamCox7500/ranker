package com.samcox.ranker.media;

import jakarta.validation.constraints.NotNull;

/**
 * DTO for a request to add a new entry to a {@link MediaList}
 * Comprised of the id of the media in the TMDB API and the ranking of that media in the media list e.g. number 1.
 *
 * @see MediaList
 */
public class EntryAddRequest {
  /**
   * The id of the media in TMDB.
   */
  @NotNull
  private Long tmdbId;
  /**
   * A number representing the ranked position of this media in the user's media list.
   * E.g. 1 means it is the number 1 ranked piece of media. Higher ranked than all other media.
   */
  private int ranking;

  /**
   * Returns the TMDB id of the media in the request.
   * @return the id of the media in TMDB API.
   */
  public Long getTmdbId() {
    return tmdbId;
  }

  /**
   * Sets the TMDB id of the media in the request.
   * @param tmdbId the id of the media in the request.
   */
  public void setTmdbId(Long tmdbId) {
    this.tmdbId = tmdbId;
  }

  /**
   * Returns the ranking of the media in the request.
   * @return the ranking of the media in the request
   */
  public int getRanking() {
    return ranking;
  }
  public void setRanking(int ranking) {
    this.ranking = ranking;
  }
}
