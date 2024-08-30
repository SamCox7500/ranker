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

  public Long getId() {
    return id;
  }
  private void reorderEntries() {
    for (int i = 0; i < entries.size(); i++) {
      entries.get(i).setRanking(i + 1);
    }
  }
}
