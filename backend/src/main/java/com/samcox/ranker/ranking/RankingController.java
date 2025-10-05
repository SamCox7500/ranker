package com.samcox.ranker.ranking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * REST controller for handling numbered ranking operations.
 *
 * <p>This controller provides endpoints to perform CRUD operations on {@link Ranking}s
 *  including retrieving a specific ranking by id, and retrieving all rankings by user.</p>
 *
 * @see Ranking
 * @see RankingService
 * @see RankingDTO
 */
@RestController
public class RankingController {

  /**
   * The service layer that the controller uses to perform ranking related operations. See {@link RankingService}.
   */
  private final RankingService rankingService;

  /**
   * Creates a new ranking controller instance
   * @param rankingService the service to be used by the controller for performing operations on rankings.
   */
  public RankingController(RankingService rankingService) {
    this.rankingService = rankingService;
  }

  /**
   * Returns all rankings for belonging to a specific user
   * @param userId the id of the user that owns the rankings
   * @return a list of all rankings as DTOs belonging to the user
   * @throws AccessDeniedException if the current authenticated user does not have permission to access the rankings
   */
  @GetMapping("/users/{userId}/rankings")
  public List<RankingDTO> getAllRankings(@PathVariable("userId") Long userId) throws AccessDeniedException {
    return RankingDTOMapper.toRankingDTOs(rankingService.getAllRankingsByUser(userId));
  }

  /**
   * Returns a ranking belonging to a user as specified by ranking id.
   * @param userId the id of the user that owns the ranking
   * @param rankerId the id of the ranking to be retrieved
   * @return the specified ranking as a DTO
   * @throws AccessDeniedException if the current authenticated user does not have permission to access this ranking
   */
  @GetMapping("/users/{userId}/rankings/{rankingId}")
  public RankingDTO getRanking(@PathVariable("userId") Long userId, @PathVariable("rankingId") Long rankerId) throws AccessDeniedException {
    return RankingDTOMapper.toRankingDTO(rankingService.getRankingByIdAndUser(rankerId, userId));
  }

}
