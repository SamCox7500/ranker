package com.samcox.ranker.media;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public abstract class MediaList {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Enumerated(EnumType.STRING)
  private MediaType mediaType;

  @OneToMany(mappedBy = "mediaList", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MediaListEntry> entries = new ArrayList<>();

/*
  public void addEntry(T entry) {
    entries.add(entry);
    entry.setMediaList(this);
  }
  public void removeEntry(T entry) {
    entries.remove(entry);
    entry.setMediaList(null);
  }
  public Long getId() {
    return id;
  }
  public List<T> getEntries() {
    return entries;
  }
*/
}
