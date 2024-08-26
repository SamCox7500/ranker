package com.samcox.ranker.media;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class MediaList<T extends MediaListEntry> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToMany(mappedBy = "mediaList", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<T> entries;

  public Long getId() {
    return id;
  }
  public List<T> getEntries() {
    return entries;
  }
  public void setEntries(List<T> entries) {
    this.entries = entries;
  }
  //public abstract String getMediaType();
}
