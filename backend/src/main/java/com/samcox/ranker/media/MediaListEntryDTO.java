package com.samcox.ranker.media;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A DTO for {@link MediaListEntry}.
 * @see MediaListEntry
 * @see MediaList
 */
public class MediaListEntryDTO {
  /**
   * The id of the {@link MediaListEntry}
   */
  private Long id;
  /**
   * The ranking of the entry in the {@link MediaList} that contains it
   */
  private int ranking;

  /**
   * Default constructor required by JPA.
   */
  public MediaListEntryDTO() {

  }

  /**
   * Constructor for MediaListEntry DTO. Contains
   * @param id the id of the {@link MediaListEntry}
   * @param ranking the ranking of the entry in the {@link MediaList}
   */
  public MediaListEntryDTO(Long id, int ranking) {
    this.id = id;
    this.ranking = ranking;
  }

  /**
   * Returns the id of the MediaListEntry in the DTO.
   * @return the id of the MediaListEntry
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the id of the MediaListEntry in the DTO.
   * @param id the id of the MediaListEntry
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the ranking of entry in the MediaList.
   * @return the ranking of the entry
   */
  public int getRanking() {
    return ranking;
  }

  /**
   * Sets the ranking of the entry in the MediaList.
   * @param ranking the new ranking of the entry
   */
  public void setRanking(int ranking) {
    this.ranking = ranking;
  }

}
