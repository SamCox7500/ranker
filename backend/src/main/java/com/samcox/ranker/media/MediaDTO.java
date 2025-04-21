package com.samcox.ranker.media;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.awt.image.TileObserver;

/**
 * DTO for a piece of media in a {@link MediaListEntry} of a {@link MediaList}.
 * Holds JSON data from TMDB API.
 * @see MediaListEntry
 * @see MediaList
 */
public class MediaDTO {

  /**
   * The title of the media.
   */
  @JsonProperty("title")
  private String title;

  /**
   * Constructor for the DTO.
   * @param title the title of the media
   */
  public MediaDTO(String title) {
    this.title = title;
  }

  /**
   * Sets the title of the media
   * @param title the title of the media
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the title of the media
   * @return the title of the media
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the MediaDTO as a string
   * @return the string representation of the MediaDTO
   */
  @Override
  public String toString() {
    return "MediaDTO{" +
      "title='" + title + '\'' +
      '}';
  }
}
