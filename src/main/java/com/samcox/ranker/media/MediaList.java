package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRanking;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class MediaList {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Enumerated(EnumType.STRING)
  private MediaType mediaType;

  @OneToMany(mappedBy = "mediaList", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MediaListEntry> entries = new ArrayList<>();

  @OneToOne(mappedBy = "mediaList")
  private NumberedRanking numberedRanking;

  public MediaType getMediaType() {
    return mediaType;
  }

  public void setMediaType(MediaType mediaType) {
    this.mediaType = mediaType;
  }

  public List<MediaListEntry> getEntries() {
    return entries;
  }

  public void setEntries(List<MediaListEntry> entries) {
    this.entries = entries;
  }

  public NumberedRanking getNumberedRanking() {
    return numberedRanking;
  }

  public void setNumberedRanking(NumberedRanking numberedRanking) {
    this.numberedRanking = numberedRanking;
  }

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
