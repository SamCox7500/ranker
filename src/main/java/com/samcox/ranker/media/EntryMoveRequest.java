package com.samcox.ranker.media;

public class EntryMoveRequest {
  private Long entryId;
  private int newPosition;

  public EntryMoveRequest(Long entryId, int newPosition) {
    this.entryId = entryId;
    this.newPosition = newPosition;
  }
  public Long getEntryId() {
    return entryId;
  }
  public void setEntryId(Long entryId) {
    this.entryId = entryId;
  }
  public int getNewPosition() {
    return newPosition;
  }
  public void setNewPosition(int newPosition) {
    this.newPosition = newPosition;
  }
}
