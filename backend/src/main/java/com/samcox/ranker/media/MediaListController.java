/*
package com.samcox.ranker.media;

import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;
*/
/*
 * REST controller for handling {@link MediaList} operations.
 *
 * <p>This controller provides endpoints to perform CRUD operations on {@link MediaList}s
 *  including fetching media lists, as well as adding, moving, and removing entries from media lists.</p>
 *
 * @see MediaList
 * @see MediaListEntry
 * @see NumberedRanking
 */
/*
@RestController
public class MediaListController {
*/


  /*
   * The service for performing operations on {@link MediaList}.

  private final MediaListService mediaListService;

   * The service for performing operations on {@link MediaListEntry}.

  private final MediaListEntryService mediaListEntryService;
  /*
   * Service for performing {@link NumberedRanking} related operations.

  private final NumberedRankingService numberedRankingService;

  /**
   * Constructor for media list controller.
   * @param mediaListService the service for performing operations relating to media lists
   * @param mediaListEntryService the service for performing operations relating to media list entries
   * @param numberedRankingService the service for performing operations relating to numbered rankings

  public MediaListController(MediaListService mediaListService, MediaListEntryService mediaListEntryService, NumberedRankingService numberedRankingService) {
    this.mediaListService = mediaListService;
    this.mediaListEntryService = mediaListEntryService;
    this.numberedRankingService = numberedRankingService;
  }

  /*
   * Returns a media list by user id and ranking id.
   * @param userId the id of the user that owns the media list
   * @param rankingId the id of the ranking to which the media list belongs
   * @return the media list with the specified user and ranking
   * @throws AccessDeniedException if the user trying to retrieve the media list does not have permission

  //@GetMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}")
  @GetMapping("/users/{userId}/numberedrankings/{rankingId}/medialist")
  public MediaListDTO getMediaList(@PathVariable Long userId, @PathVariable Long rankingId) throws AccessDeniedException {

    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
    return mediaListService.toMediaListDTO(mediaList);

  }

}
*/

