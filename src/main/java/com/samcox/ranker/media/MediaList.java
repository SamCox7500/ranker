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
  public void addEntry(MediaListEntry entry) {
    entry.setRanking(this.entries.size() + 1);
    entries.add(entry);
    entry.setMediaList(this);
  }
  public void removeEntry(MediaListEntry entry) {
    entries.remove(entry);
    entry.setMediaList(null);

    //reorder the remaining entries
    for (int i = 0; i < this.entries.size(); i++) {
      this.entries.get(i).setRanking(i + 1);
    }
  }
  public Long getId() {
    return id;
  }
}
