package com.samcox.ranker.media;

import com.samcox.ranker.auth.AuthService;
import jakarta.validation.Valid;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Service for performing CRUD operations on {@link MediaListEntry}.
 * <p>As most entry related operations are performed by {@link MediaListService}.
 * This service is mainly used for retrieving media list entries</p>
 * @see MediaListEntry
 * @see MediaListService
 */
public class MediaListEntryService {

  /**
   * Repository for accessing media list entries.
   */
  private final MediaListEntryRepository mediaListEntryRepository;

  /**
   * Service for authentication and authorisation checks.
   */
  private final AuthService authService;

  /**
   * Constructor for the service.
   * @param mediaListEntryRepository repository for accessing media list entries.
   * @param authService service for authentication and authorisation checks
   */
  public MediaListEntryService(MediaListEntryRepository mediaListEntryRepository, AuthService authService) {
    this.mediaListEntryRepository = mediaListEntryRepository;
    this.authService = authService;
  }

  /**
   * Returns a media list entry by its id
   * @param id the id of the media list entry
   * @return the media list entry with the specified id
   * @throws AccessDeniedException if the user trying to access the entry does not have permission
   */
  public MediaListEntry getMediaListEntryById(Long userId, Long id) throws AccessDeniedException {
    checkOwnership(userId, id);
    return mediaListEntryRepository.findById(id)
      .orElseThrow(() -> new MediaListEntryNotFoundException("MediaListEntry not found with id: " + id));
  }

  /**
   * Returns the media list entry by media list and id.
   * @param mediaList the media list containing the entry
   * @param id the id of the entry
   * @return the media list entry with the specified media list and id
   */
  public MediaListEntry getMediaListEntryByMediaListAndId(MediaList mediaList, Long id) {
    return mediaListEntryRepository.findByMediaListAndId(mediaList, id)
      .orElseThrow(() -> new MediaListEntryNotFoundException("MediaListEntry not found for MediaList: "
        + mediaList.getId() + " and entry id: " + id));
  }

  /**
   * Checks if the user trying to access an entry has the permission to do so. Throws an exception if not.
   * @param mediaListEntryId the id of the entry attempting to be accessed
   * @throws AccessDeniedException if the user does not have permission to access the entry
   */
  public void checkOwnership(Long userId, Long mediaListEntryId) throws AccessDeniedException {
    Long authUserId = authService.getAuthenticatedUser().getId();
    MediaListEntry mediaListEntry = mediaListEntryRepository.findById(mediaListEntryId)
      .orElseThrow(() -> new MediaListEntryNotFoundException("Could not check ownership as mediaListEntry does not exist with id: " + mediaListEntryId));
    if (!userId.equals(authUserId)) {
      throw new AccessDeniedException("You do not have permission to access that resource");
    }
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
}
