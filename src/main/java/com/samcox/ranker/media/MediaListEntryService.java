package com.samcox.ranker.media;

import jakarta.validation.Valid;

import java.util.List;

public class MediaListEntryService {

  private final MediaListEntryRepository mediaListEntryRepository;

  public MediaListEntryService(MediaListEntryRepository mediaListEntryRepository, MediaListService mediaListService) {
    this.mediaListEntryRepository = mediaListEntryRepository;
  }
  public MediaListEntry getMediaListEntryById(Long id) {
    return mediaListEntryRepository.findById(id)
      .orElseThrow(() -> new MediaListEntryNotFoundException("MediaListEntry not found with id: " + id));
  }
  public MediaListEntry getMediaListEntryByMediaListAndRanking(MediaList mediaList, int ranking) {
    return mediaListEntryRepository.findByMediaListAndRanking(mediaList, ranking)
      .orElseThrow(() -> new MediaListEntryNotFoundException("MediaListEntry not found with MediaList: "
        + mediaList.getId() + " and ranking position: " + ranking));
  }
  public List<MediaListEntry> getMediaListEntriesByMediaList(MediaList mediaList) {
    return mediaListEntryRepository.findByMediaList(mediaList)
      .orElseThrow(() -> new MediaListEntryNotFoundException("MediaListEntries not found for MediaList: " + mediaList.getId()));
  }
  public void createMediaListEntry(@Valid MediaListEntryDTO mediaListEntryDTO) {
    MediaListEntry entry = new MediaListEntry();
    entry.setMediaList(mediaListEntryDTO.getMediaList());
    entry.setRanking(mediaListEntryDTO.getRanking());
    mediaListEntryRepository.save(entry);
  }
  public void updateMediaListEntry(@Valid MediaListEntryDTO mediaListEntryDTO) {

    MediaListEntry entry = mediaListEntryRepository.findById(mediaListEntryDTO.getId())
      .orElseThrow(() -> new MediaListEntryNotFoundException("MediaListEntry not found for media list with id: "
        + mediaListEntryDTO.getId()));

    entry.setRanking(mediaListEntryDTO.getRanking());
    mediaListEntryRepository.save(entry);
  }
  public void deleteMediaListEntry(Long id) {
    mediaListEntryRepository.deleteById(id);
  }
}
