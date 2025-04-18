package com.samcox.ranker.media;

import jakarta.persistence.*;

/**
 * Represents a ranked media entry in a {@link MediaList}
 * @see MediaList
 */
@Entity
public class MediaListEntry {

  /**
   * The id of the entry
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /**
   * The ranked position of the entry e.g. number 1.
   */
  private int ranking;

  /**
   * The id of the media being ranked in TMDB API.
   */
  private Long tmdbId;

  /**
   * The media list that contains the entry.
   */
  @ManyToOne
  @JoinColumn(name = "media_list_id")
  private MediaList mediaList;

  /**
   * Returns the id of the entry.
   * @return the id of the entry
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the ranking of the entry.
   * @return the ranking of the entry
   */
  public int getRanking() {
    return ranking;
  }

  /**
   * Sets the ranking of the entry.
   * @param ranking the ranking of the entry
   */
  public void setRanking(int ranking) {
    this.ranking = ranking;
  }

  /**
   * Returns the media list of the entry.
   * @return the media list of the entry
   */
  public MediaList getMediaList() {
    return mediaList;
  }

  /**
   * Sets the media list of the entry.
   * @param mediaList the media list of the entry
   */
  public void setMediaList(MediaList mediaList) {
    this.mediaList = mediaList;
  }

  /**
   * Returns the TMDb ID of the media being ranked.
   * @return the id of the media in TMDB API
   */
  public Long getTmdbId() {
    return tmdbId;
  }

  /**
   * Sets the id of the media
   * @param tmdbId the new id of the media being ranked
   */
  public void setTmdbId(Long tmdbId) {
    this.tmdbId = tmdbId;
  }

  /**
   * Returns the media list entry as a string
   * @return the string representation of the entry
   */
  @Override
  public String toString() {
    return "MediaListEntry{" +
      "id=" + id +
      ", ranking=" + ranking +
      ", tmdbId=" + tmdbId +
      '}';
  }
}
