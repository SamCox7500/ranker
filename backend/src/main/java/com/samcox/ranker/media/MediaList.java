package com.samcox.ranker.media;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a list of {@link MediaListEntry} of a specific {@link MediaType}.
 *
 * <p>This entity is mapped to the "MediaList" table in the database.
 *  * All attributes have validation constraints.</p>
 *
 * @see MediaType
 * @see MediaListEntry
 */
@Entity
public class MediaList {

  /**
   * Id of the media list.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /**
   * The type of media that is ranked by the entries in the media list.
   */
  @Enumerated(EnumType.STRING)
  private MediaType mediaType;

  /**
   * The ranked list of media.
   */
  @OneToMany(mappedBy = "mediaList", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("ranking ASC")
  private List<MediaListEntry> entries = new ArrayList<>();


  /**
   * The overarching data on the media list, such as the title, desc, etc.
   */
  /*
  @OneToOne(mappedBy = "mediaList", cascade = CascadeType.ALL)
  private NumberedRanking numberedRanking;
  */

  /**
   * The type of media that the media list contains
   * @return the MediaType of the media list.
   */
  public MediaType getMediaType() {
    return mediaType;
  }

  /**
   * Sets the media type of the media list
   * @param mediaType thge media type of the media list
   */
  public void setMediaType(MediaType mediaType) {
    this.mediaType = mediaType;
  }

  /**
   * Returns the list of all media list entries contained within the list
   * @return the list of all media list entries
   */
  public List<MediaListEntry> getEntries() {
    //return entries.stream().sorted(Comparator.comparingInt(MediaListEntry::getRanking)).collect(Collectors.toList());
    return entries;
  }

  /**
   * Sets the list of media list entries
   * @param entries the list of media list entries
   */
  public void setEntries(List<MediaListEntry> entries) {
    this.entries = entries;
  }

  /**
   * Returns the numbered ranking that owns the media list
   * @return the numbered ranking attached to the media list
   */
  /*
  public NumberedRanking getNumberedRanking() {
    return numberedRanking;
  }
  */
  /**
   *
   * Sets the numbered ranking of the media list
   * @param numberedRanking the numbered ranking of the media list
   */
  /*
  public void setNumberedRanking(NumberedRanking numberedRanking) {
    this.numberedRanking = numberedRanking;
  }
  */

  /**
   * Adds a new {@link MediaListEntry} to the media list.
   * @param entry the new entry to be added
   */
  public void addEntry(MediaListEntry entry) {
    entries.add(entry.getRanking()-1, entry);
    entry.setMediaList(this);
    reorderEntries();
  }

  /**
   * Removes a specified entry from the media list.
   * @param entry the entry to be removed
   */
  public void removeEntry(MediaListEntry entry) {
    entries.remove(entry);
    entry.setMediaList(null);

    //reorder the remaining entries
    reorderEntries();
  }

  /**
   * Removes a series of entries from the media list
   * @param entries the list of all entries to be removed
   */
  public void removeEntries(List<MediaListEntry> entries) {
    for (MediaListEntry entry: entries) {
      entry.setMediaList(null);
    }
    this.entries.removeAll(entries);
    reorderEntries();
  }

  /**
   * Moves an entry in the media list from its old ranking position to its new ranking position.
   * @param oldPosition the former position of the entry
   * @param newPosition the new ranking for the entry
   */
  public void moveEntry(int oldPosition, int newPosition) {

    if (oldPosition < 1 || newPosition < 1 || oldPosition > entries.size() || newPosition > entries.size()) {
      throw new IllegalArgumentException("Invalid positions");
    }

    MediaListEntry entry = entries.remove(oldPosition - 1);

    entries.add(newPosition - 1, entry);

    reorderEntries();
  }

  /**
   * Returns if there exists a {@link MediaListEntry} with a specified TMDB id in this media list.
   * @param tmdbId the id of the TMDB media to be looked for
   * @return {@code true} if there exists an entry containing the specified TMDB id, {@code false} otherwise
   */
  public boolean hasEntryWithTmdbId(Long tmdbId) {
    return entries.stream().anyMatch(entry -> entry.getTmdbId().equals(tmdbId));
  }

  /**
   * Returns the id of the media list.
   * @return the id of the media list
   */
  public Long getId() {
    return id;
  }
  private void reorderEntries() {
    for (int i = 0; i < entries.size(); i++) {
      entries.get(i).setRanking(i + 1);
    }
  }

  /**
   * Returns the media list as a string.
   * @return the string representation of the media list
   */
  @Override
  public String toString() {
    return "MediaList{" +
      "id=" + id +
      ", mediaType=" + mediaType +
      ", entries=" + entries +
      //", numberedRanking=" + numberedRanking +
      '}';
  }
}
