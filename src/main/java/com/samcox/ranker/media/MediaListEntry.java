package com.samcox.ranker.media;

import jakarta.persistence.*;

@Entity
public class MediaListEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private int ranking;

  private String tmdbId;

  @ManyToOne
  @JoinColumn(name = "media_list_id")
  private MediaList mediaList;

  public Long getId() {
    return id;
  }

  public int getRanking() {
    return ranking;
  }

  public void setRanking(int ranking) {
    this.ranking = ranking;
  }

  public MediaList getMediaList() {
    return mediaList;
  }

  public void setMediaList(MediaList mediaList) {
    this.mediaList = mediaList;
  }

  public String getTmdbId() {
    return tmdbId;
  }
  public void setTmdbId(String tmdbId) {
    this.tmdbId = tmdbId;
  }
}
