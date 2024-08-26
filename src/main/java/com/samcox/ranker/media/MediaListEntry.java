package com.samcox.ranker.media;

import jakarta.persistence.*;

@MappedSuperclass
public class MediaListEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private int ranking;

  @ManyToOne
  @JoinColumn
  private MediaList<?> mediaList;

  public Long getId() {
    return id;
  }
  public int getRanking() {
    return ranking;
  }
  public void setRanking(int ranking) {
    this.ranking = ranking;
  }
  public MediaList<?> getMediaList() {
    return mediaList;
  }
  public void setMediaList(MediaList<?> mediaList) {
    this.mediaList = mediaList;
  }
}
