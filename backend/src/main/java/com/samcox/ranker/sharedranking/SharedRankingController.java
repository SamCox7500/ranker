package com.samcox.ranker.sharedranking;

import com.samcox.ranker.media.MediaListEntryService;
import com.samcox.ranker.media.MediaListService;
import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingDTO;
import com.samcox.ranker.numberedranking.NumberedRankingDTOMapper;
import com.samcox.ranker.numberedranking.NumberedRankingService;
import com.samcox.ranker.ranking.Ranking;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.Map;

/**
 * REST controller for handling shared ranking operations.
 *
 * <p>This controller uses the shared ranking service to perform operations on shared rankings such as:
 * creating, deleting, and viewing them. </p>
 *
 * @see SharedRanking
 * @see SharedRankingService
 * @see SharedRankingRepository
 */
@RestController
public class SharedRankingController {

  /**
   * Service for performing CRUD operations on shared rankings.
   */
  private final SharedRankingService sharedRankingService;
  /**
   * Service for performing CRUD operations on media lists.
   */
  private final MediaListService mediaListService;

  /**
   * Creates a new shared ranking controller instance.
   * @param sharedRankingService service used to perform CRUD operations on shared rankings.
   * @param mediaListService service used to perform CRUD operations on media lists.
   */
  public SharedRankingController(SharedRankingService sharedRankingService, MediaListService mediaListService) {
    this.sharedRankingService = sharedRankingService;
    this.mediaListService = mediaListService;
  }

  /**
   * Creates a shared ranking from a ranking.
   * @param rankingId the id of the ranking to be shared
   * @param userId the id of the owner of the ranking
   * @return JSON response with unique share token for the shared ranking
   * @throws AccessDeniedException if the user trying to share the ranking does not own it
   */
  @PostMapping("/users/{userId}/rankings/{rankingId}/shared")
  public ResponseEntity<?> shareRanking(@PathVariable Long rankingId, @PathVariable Long userId) throws AccessDeniedException {
    sharedRankingService.shareRanking(rankingId, userId);

    return ResponseEntity.ok(Map.of(
      "message", "Ranking shared successfully",
      "sharedToken", sharedRankingService.getSharedRanking(rankingId, userId).getShareToken()
    ));
  }

  /**
   * Removes a shared ranking.
   * @param rankingId the id of the ranking to be unshared
   * @param userId the id of the user that owns the ranking
   * @return JSON response with message indicating the ranking was unshared
   * @throws AccessDeniedException if the user trying to unshare the ranking does not own it
   */
  @DeleteMapping("/users/{userId}/rankings/{rankingId}/shared")
  public ResponseEntity<?> unshareRanking(@PathVariable Long rankingId, @PathVariable Long userId) throws AccessDeniedException {
    sharedRankingService.unshareRanking(rankingId, userId);
    return ResponseEntity.ok(Map.of("message", "Ranking unshared successfully"));
  }

  /**
   * Returns the share token associated with a shared ranking.
   * @param rankingId id of the ranking that has been shared
   * @param userId id of the owner of the ranking
   * @return JSON response containing the share token used to access the shared ranking
   * @throws AccessDeniedException if the user trying to access the share token is not the owner of the ranking
   */
  @GetMapping("/users/{userId}/rankings/{rankingId}/shared")
  public ResponseEntity<?> getShareInfo(@PathVariable Long rankingId, @PathVariable Long userId) throws AccessDeniedException {
    SharedRanking sharedRanking = sharedRankingService.getSharedRanking(rankingId, userId);
    return ResponseEntity.ok(Map.of("sharedToken", sharedRanking.getShareToken()));
  }

  /**
   * Returns the ranking associated with a shared ranking. Allows non-owners to view a ranking using the share token.
   * @param shareToken unique string used to access the shared ranking
   * @return JSON response containing dto of the ranking that is shared
   */
  @GetMapping("/sharedrankings/{shareToken}")
  public ResponseEntity<?> viewSharedRanking(@PathVariable String shareToken) {
    Ranking ranking = sharedRankingService.viewSharedRanking(shareToken);
    if (ranking instanceof NumberedRanking numberedRanking) {
      NumberedRankingDTO numberedRankingDTO = NumberedRankingDTOMapper.toNumberedRankingDTO(numberedRanking);
      numberedRankingDTO.setMediaListDTO(mediaListService.toMediaListDTO(numberedRanking.getMediaList(), numberedRanking.getMediaType()));
      return ResponseEntity.ok(numberedRankingDTO);
    }
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message", "No ranking of a valid ranking type exsits for that share token"));
  }
  @GetMapping("/sharedrankings/{shareToken}/lookup")
  public ResponseEntity<SharedRankingLookupDTO> lookupSharedRanking(@PathVariable String shareToken) {
    Ranking ranking = sharedRankingService.viewSharedRanking(shareToken);
    String rankingType = ranking.getRankingType().toString();
    return ResponseEntity.ok(new SharedRankingLookupDTO(rankingType, shareToken));
  }
}
