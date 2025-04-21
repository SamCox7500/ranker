package com.samcox.ranker.media;

/**
 * A DTO for a request to change the position of a {@link MediaListEntry} in a {@link MediaList}.
 * Example: Changing the number one ranked item to now be the number 2 ranked item.
 *
 * @see MediaListEntry
 * @see MediaList
 */
public class EntryMoveRequest {
  /**
   * The id of the {@link MediaListEntry} to be moved.
   */
  private Long entryId;
  /**
   * The new ranking of the {@link MediaListEntry} in the list.
   */
  private int newPosition;

  /**
   * Constructor for the move request.
   * @param entryId the id of the entry to be moved in the list
   * @param newPosition the new ranking of the entry to be moved
   */
  public EntryMoveRequest(Long entryId, int newPosition) {
    this.entryId = entryId;
    this.newPosition = newPosition;
  }

  /**
   * Returns the id of the entry to be moved in the request
   * @return the id of the entry to be moved
   */
  public Long getEntryId() {
    return entryId;
  }

  /**
   * Sets the id of the entry to be moved
   * @param entryId the id of the {@link MediaListEntry} to be moved
   */
  public void setEntryId(Long entryId) {
    this.entryId = entryId;
  }

  /**
   * Returns the new ranking that the entry is to be moved to
   * @return the new position of the entry in the list
   */
  public int getNewPosition() {
    return newPosition;
  }

  /**
   * Sets the new ranking for the entry to be moved to
   * @param newPosition the new ranking of the entry
   */
  public void setNewPosition(int newPosition) {
    this.newPosition = newPosition;
  }
}
