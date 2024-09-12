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

  @OneToOne(mappedBy = "mediaList", cascade = CascadeType.ALL)
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
    entries.add(entry.getRanking()-1, entry);
    entry.setMediaList(this);
    reorderEntries();
  }
  public void removeEntry(MediaListEntry entry) {
    entries.remove(entry);
    entry.setMediaList(null);

    //reorder the remaining entries
    reorderEntries();
  }
  public void removeEntries(List<MediaListEntry> entries) {
    for (MediaListEntry entry: entries) {
      entry.setMediaList(null);
    }
    this.entries.removeAll(entries);
    reorderEntries();
  }
  public void moveEntry(int oldPosition, int newPosition) {
    if (oldPosition < 1 || newPosition < 1 || oldPosition > entries.size() || newPosition > entries.size()) {
      throw new IllegalArgumentException("Invalid positions");
    }

    //Stores the removed element that is returned
    MediaListEntry entry = entries.remove(oldPosition - 1);

    //Add the element back to the array list in new position
    entries.add(newPosition - 1, entry);

    reorderEntries();
  }
  public boolean hasEntryWithTmdbId(Long tmdbId) {
    return entries.stream().anyMatch(entry -> entry.getTmdbId().equals(tmdbId));
  }

  public Long getId() {
    return id;
  }
  private void reorderEntries() {
    for (int i = 0; i < entries.size(); i++) {
      entries.get(i).setRanking(i + 1);
    }
  }
  @Override
  public String toString() {
    return "MediaList{" +
      "id=" + id +
      ", mediaType=" + mediaType +
      ", entries=" + entries +
      ", numberedRanking=" + numberedRanking +
      '}';
  }
}
