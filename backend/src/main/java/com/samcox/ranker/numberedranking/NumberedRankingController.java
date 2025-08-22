package com.samcox.ranker.numberedranking;

import com.samcox.ranker.media.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * REST controller for handling numbered ranking operations.
 *
 * <p>This controller provides endpoints to perform CRUD operations on {@link NumberedRanking}s
 *  including creating a numbered ranking, retrieving one, updating a numbered ranking,
 *  and deleting a numbered ranking.</p>
 *
 * @see NumberedRanking
 * @see NumberedRankingService
 * @see NumberedRankingDTO
 */
@RestController
public class NumberedRankingController {

  /**
   * The service layer the controller uses to perform numbered ranking operations. See {@link NumberedRankingService}.
   */
  private final NumberedRankingService numberedRankingService;

  private final MediaListService mediaListService;

  private final MediaListEntryService mediaListEntryService;

  /**
   * Creates a new numbered ranking controller instance.
   * @param numberedRankingService the service to be used by the controller for performing operations on numbered rankings.
   */
  public NumberedRankingController(NumberedRankingService numberedRankingService, MediaListService mediaListService, MediaListEntryService mediaListEntryService) {
    this.numberedRankingService = numberedRankingService;
    this.mediaListService = mediaListService;
    this.mediaListEntryService = mediaListEntryService;
  }

  /**
   * Returns a list of all numbered rankings belonging to a user.
   * @param userId the id of the user used to fetch all numbered rankings for
   * @return the list of all numbered rankings as DTOs
   * @throws AccessDeniedException if the authenticated user does not have authorisation to access rankings for that user.
   */
  @GetMapping("/users/{userId}/numberedrankings")
  public List<NumberedRankingDTO> getAllRankings(@PathVariable("userId") Long userId) throws AccessDeniedException {
    return NumberedRankingDTOMapper.toNumberedRankingDTOs(numberedRankingService
      .getAllNumberedRankingsByUser(userId));
  }

  /**
   * Returns a numbered ranking by id and userId.
   * @param rankingId the id of the ranking to be fetched.
   * @param userId the owner of the ranking to be fetched
   * @return the numbered ranking as a DTO
   * @throws AccessDeniedException if the current authenticated user is not authorised to access this numbered ranking
   */
  @GetMapping("/users/{userId}/numberedrankings/{rankingId}")
  public NumberedRankingDTO getRanking(@PathVariable("rankingId") Long rankingId, @PathVariable("userId") Long userId) throws AccessDeniedException {
    return NumberedRankingDTOMapper.toNumberedRankingDTO(numberedRankingService.getNumberedRankingByIdAndUser(rankingId, userId));
  }

  /**
   * Creates a new numbered ranking.
   * @param userId the id of the user creating the ranking
   * @param createNumberedRankingDTO the DTO of ranking information used to create the numbered ranking. See {@link CreateNumberedRankingDTO}.
   * @throws AccessDeniedException if the currently authenticated user does not have permission to create a numbered ranking.
   * Either because they are not authenticated or because they are trying to create a ranking for a different user.
   */
  //todo should service method have id separate as well?
  @PostMapping("/users/{userId}/numberedrankings")
  public void createNumberedRanking(@PathVariable("userId") Long userId, @RequestBody @Valid CreateNumberedRankingDTO createNumberedRankingDTO) throws AccessDeniedException {
    numberedRankingService.createNumberedRanking(userId, createNumberedRankingDTO);
  }

  /**
   * Updates a numbered ranking.
   * @param userId the id of the user that the ranking belongs to
   * @param updateNumberedRankingDTO the new data used to update the numbered ranking. See {@link UpdateNumberedRankingDTO}.
   * @throws AccessDeniedException if the currently authenticated user does not have permission to update this ranking
   */
  @PutMapping("/users/{userId}/numberedrankings/{rankingId}")
  public void updateRanking(@PathVariable("userId") Long userId, @PathVariable("rankingId") Long rankingId, @RequestBody @Valid UpdateNumberedRankingDTO updateNumberedRankingDTO) throws AccessDeniedException {
    numberedRankingService.updateNumberedRanking(rankingId, userId, updateNumberedRankingDTO);
  }

  /**
   * Deletes the numbered ranking
   * @param rankingId the id of the numbered ranking to be deleted
   * @param userId the id of the user to whom the numbered ranking belongs
   * @throws AccessDeniedException if the currently authenticated user does not have permission to delete this ranking
   */
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}")
  public void deleteRanking(@PathVariable("rankingId") Long rankingId, @PathVariable("userId") Long userId) throws AccessDeniedException {
    numberedRankingService.deleteNumberedRankingByIdAndUser(rankingId, userId);
  }

  /**
   * Adds a new media list entry to the numbered ranking.
   * @param userId the id of the user that owns the media list
   * @param rankingId the id of the numbered ranking to which the media list belongs
   * @param entryAddRequest DTO containing the data used to add a new entry to the media list
   * @throws AccessDeniedException if the user trying to add a new entry does not have permission
   */
  //@PostMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries")
  @PostMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries")
  public void addEntryToMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @RequestBody @Valid EntryAddRequest entryAddRequest
  ) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByIdAndUser(rankingId, userId);
    MediaList mediaList = numberedRanking.getMediaList();
    mediaListService.addEntryToList(userId,entryAddRequest, mediaList.getId());
  }
  /**
   * Moves a media list entry in the numbered ranking to a different ranked position. E.g., move the number 1 ranked item to now be number 2.
   * @param userId the id of the user that owns the ranking
   * @param rankingId the id of the numbered ranking to which the media list belongs
   * @param entryId the id of the entry to be moved
   * @param entryMoveRequest DTO containing data used to move the entry such as its new position
   * @throws AccessDeniedException if the user trying to move the entry does not have permission
   */
  @PutMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries/{entryId}")
  @Transactional
  public void moveEntryInMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @PathVariable Long entryId,
    @RequestBody @Valid EntryMoveRequest entryMoveRequest
  ) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByIdAndUser(rankingId, userId);
    MediaList mediaList = numberedRanking.getMediaList();
    MediaListEntry mediaListEntry = mediaListEntryService.getMediaListEntryById(userId,entryMoveRequest.getEntryId());
    int oldPosition = mediaListEntry.getRanking();
    int newPosition = entryMoveRequest.getNewPosition();
    mediaListService.moveEntryInList(userId,mediaList.getId(), oldPosition, newPosition);
  }
  /**
   * Deletes a media list entry from a numbered ranking.
   * @param userId the id of the user that owns the media list
   * @param rankingId the id of the numbered ranking to which the media list belongs
   * @param entryId the id of the entry to be deleted
   * @throws AccessDeniedException if the user trying to delete the ranking does not have permission to do so
   */
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries/{entryId}")
  public void removeEntryFromMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @PathVariable Long entryId
  ) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByIdAndUser(rankingId, userId);
    MediaList mediaList = numberedRanking.getMediaList();
    mediaListService.removeEntryInList(userId,mediaList.getId(), entryId);
  }
  /**
   * Deletes multiple specified media list entries from a numbered ranking.
   * @param userId the id of the user that owns the media list
   * @param rankingId the id of the ranking to which the media list belongs
   * @param entryIds a list of ids of the entries to be deleted
   * @throws AccessDeniedException if the user trying to delete the entries does not have permission
   */
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries")
  public void removeEntriesFromMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @RequestBody List<Long> entryIds
  ) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByIdAndUser(rankingId, userId);
    MediaList mediaList = numberedRanking.getMediaList();
    mediaListService.removeEntriesInList(userId,mediaList.getId(), entryIds);
  }
}
