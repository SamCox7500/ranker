package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRanking;
import com.samcox.ranker.ranking.NumberedRankingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * REST controller for handling {@link MediaList} operations.
 *
 * <p>This controller provides endpoints to perform CRUD operations on {@link MediaList}s
 *  including fetching media lists, as well as adding, moving, and removing entries from media lists.</p>
 *
 * @see MediaList
 * @see MediaListEntry
 * @see NumberedRanking
 */
@RestController
public class MediaListController {

  /**
   * The service for performing operations on {@link MediaList}.
   */
  private final MediaListService mediaListService;
  /**
   * The service for performing operations on {@link MediaListEntry}.
   */
  private final MediaListEntryService mediaListEntryService;
  /**
   * Service for performing {@link NumberedRanking} related operations.
   */
  private final NumberedRankingService numberedRankingService;

  /**
   * Constructor for media list controller.
   * @param mediaListService the service for performing operations relating to media lists
   * @param mediaListEntryService the service for performing operations relating to media list entries
   * @param numberedRankingService the service for performing operations relating to numbered rankings
   */
  public MediaListController(MediaListService mediaListService, MediaListEntryService mediaListEntryService, NumberedRankingService numberedRankingService) {
    this.mediaListService = mediaListService;
    this.mediaListEntryService = mediaListEntryService;
    this.numberedRankingService = numberedRankingService;
  }

  /**
   * Returns a media list by user id and ranking id.
   * @param userId the id of the user that owns the media list
   * @param rankingId the id of the ranking to which the media list belongs
   * @return the media list with the specified user and ranking
   * @throws AccessDeniedException if the user trying to retrieve the media list does not have permission
   */
  //@GetMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}")
  @GetMapping("/users/{userId}/numberedrankings/{rankingId}/medialist")
  public MediaListDTO getMediaList(@PathVariable Long userId, @PathVariable Long rankingId) throws AccessDeniedException {

    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
    return mediaListService.toMediaListDTO(mediaList);

  }

  /**
   * Adds a new entry to the media list.
   * @param userId the id of the user that owns the media list
   * @param rankingId the id of the ranking to which the media list belongs
   * @param entryAddRequest DTO containing the data used to add a new entry to the media list
   * @throws AccessDeniedException if the user trying to add a new entry does not have permission
   */
  //@PostMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries")
  @PostMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries")
  public void addEntryToMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @RequestBody @Valid EntryAddRequest entryAddRequest
  ) throws AccessDeniedException {
    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
    mediaListService.addEntryToList(entryAddRequest, mediaList.getId());
  }

  /**
   * Moves an entry in the media list to a different ranked position. E.g., move the number 1 ranked item to now be number 2.
   * @param userId the id of the user that owns the ranking
   * @param rankingId the id of the ranking to which the media list belongs
   * @param entryId the id of the entry to be moved
   * @param entryMoveRequest DTO containing data used to move the entry such as its new position
   * @throws AccessDeniedException if the user trying to move the entry does not have permission
   */
  //@PutMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries/{entryId}")
  @PutMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries/{entryId}")
  @Transactional
  public void moveEntryInMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @PathVariable Long entryId,
    @RequestBody @Valid EntryMoveRequest entryMoveRequest
  ) throws AccessDeniedException {
      MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
      MediaListEntry mediaListEntry = mediaListEntryService.getMediaListEntryById(entryMoveRequest.getEntryId());
      int oldPosition = mediaListEntry.getRanking();
      int newPosition = entryMoveRequest.getNewPosition();
      mediaListService.moveEntryInList(mediaList.getId(), oldPosition, newPosition);

      //todo change to be mediaListId, mediaListEntryId, newPosition
  }

  /**
   * Deletes an entry from a media list.
   * @param userId the id of the user that owns the media list
   * @param rankingId the id of the ranking to which the media list belongs
   * @param entryId the id of the entry to be deleted
   * @throws AccessDeniedException if the user trying to delete the ranking does not have permission to do so
   */
  //@DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries/{entryId}")
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries/{entryId}")
  public void removeEntryFromMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @PathVariable Long entryId
  ) throws AccessDeniedException {
      MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
      mediaListService.removeEntryInList(mediaList.getId(), entryId);
  }

  /**
   * Deletes multiple specified entries from a media list.
   * @param userId the id of the user that owns the media list
   * @param rankingId the id of the ranking to which the media list belongs
   * @param entryIds a list of ids of the entries to be deleted
   * @throws AccessDeniedException if the user trying to delete the entries does not have permission
   */
  //@DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries")
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries")
  public void removeEntriesFromMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @RequestBody List<Long> entryIds
    ) throws AccessDeniedException {
    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
    mediaListService.removeEntriesInList(mediaList.getId(), entryIds);
  }
}
