package com.samcox.ranker.media;

import com.samcox.ranker.auth.AuthService;
import jakarta.validation.Valid;

import java.nio.file.AccessDeniedException;
import java.util.List;

public class MediaListEntryService {

  private final MediaListEntryRepository mediaListEntryRepository;

  private final AuthService authService;

  public MediaListEntryService(MediaListEntryRepository mediaListEntryRepository, AuthService authService) {
    this.mediaListEntryRepository = mediaListEntryRepository;
    this.authService = authService;
  }
  public MediaListEntry getMediaListEntryById(Long id) throws AccessDeniedException {
    checkOwnership(id);
    return mediaListEntryRepository.findById(id)
      .orElseThrow(() -> new MediaListEntryNotFoundException("MediaListEntry not found with id: " + id));
  }
  public MediaListEntry getMediaListEntryByMediaListAndId(MediaList mediaList, Long id) {
    return mediaListEntryRepository.findByMediaListAndId(mediaList, id)
      .orElseThrow(() -> new MediaListEntryNotFoundException("MediaListEntry not found for MediaList: "
        + mediaList.getId() + " and entry id: " + id));
  }
  /*
  public MediaListEntry getMediaListEntryByMediaListAndRanking(MediaList mediaList, int ranking) throws AccessDeniedException {
    checkOwnership(mediaList.getId());
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
   */
  public void checkOwnership(Long mediaListEntryId) throws AccessDeniedException {
    Long authUserId = authService.getAuthenticatedUser().getId();
    MediaListEntry mediaListEntry = mediaListEntryRepository.findById(mediaListEntryId)
      .orElseThrow(() -> new MediaListEntryNotFoundException("Could not check ownership as mediaListEntry does not exist with id: " + mediaListEntryId));
    if (!mediaListEntry.getMediaList().getNumberedRanking().getUser().getId().equals(authUserId)) {
      throw new AccessDeniedException("You do not have permission to access that resource");
    }
  }
}
