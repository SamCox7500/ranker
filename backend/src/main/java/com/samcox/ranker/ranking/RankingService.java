package com.samcox.ranker.ranking;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Service for performing CRUD operations on {@link Ranking}.
 *
 * <p>Marked as {@code @Validated} to support validation on method arguments.</p>
 *
 * @see Ranking
 */
@Validated
@Service
public class RankingService {

  /**
   * Repository for accessing rankings.
   */
  private final RankingRepository rankingRepository;
  /**
   * Service for performing CRUD operations on users.
   */
  private final UserService userService;
  /**
   * Service for performing authorisation and authentication checks.
   */
  private final AuthService authService;

  /**
   * Constructor for ranking service.
   * @param rankingRepository repository for accessing rankings
   * @param userService service for performing CRUD operations on users
   * @param authService service for performing auth checks
   */
  public RankingService(RankingRepository rankingRepository, UserService userService, AuthService authService) {
    this.rankingRepository = rankingRepository;
    this.userService = userService;
    this.authService = authService;
  }

  /**
   * Returns a ranking specified by its id and the user that owns it.
   * @param rankingId the id of the ranking to be fetched
   * @param userId the id of the user fetching the ranking
   * @return the ranking belonging to the user
   * @throws AccessDeniedException if the user trying to access the ranking is not the owner
   */
  public Ranking getRankingByIdAndUser(Long rankingId, Long userId) throws AccessDeniedException {
    checkPermissions(userId);
    User user = userService.getUserByID(userId);
    return rankingRepository.findByIdAndUser(rankingId, user)
      .orElseThrow(() -> new RankingNotFoundException("Ranking not found for ranking " + rankingId + " and user " + userId));
  }

  /**
   * Returns a list of all rankings belonging to a user.
   * @param userId the id of the user that owns all the rankings
   * @return list of all rankings belonging to the user
   * @throws AccessDeniedException if the current authenticated user is not the owner of the rankings
   */
  public List<Ranking> getAllRankingsByUser(Long userId) throws AccessDeniedException {
    checkPermissions(userId);
    User user = userService.getUserByID(userId);
    return rankingRepository.findByUser(user)
      .orElseThrow(() -> new RankingNotFoundException("No rankings found for user " + userId));
  }

  /**
   * Updates an existing ranking with new attributes.
   * @param rankingId the id of the ranking to be updated
   * @param userId the id of the user that owns the ranking
   * @param updateRankingDTO dto containing the new ranking data
   * @throws AccessDeniedException if the current authenticated user is not the owner of the ranking
   */
  public void updateRanking(Long rankingId, Long userId, @Valid UpdateRankingDTO updateRankingDTO) throws AccessDeniedException {
    checkPermissions(userId);
    if (rankingId == null) {
      throw new RankingNotFoundException("Ranking could not be found because the ID is null");
    }
    User user = userService.getUserByID(userId);
    Ranking oldRanking = rankingRepository.findByIdAndUser(rankingId, user)
      .orElseThrow(() -> new RankingNotFoundException("Ranking could not be found for ranking " + rankingId + " and user " + userId));
    oldRanking.setTitle(updateRankingDTO.getTitle());
    oldRanking.setDescription(updateRankingDTO.getDescription());
    oldRanking.setIsPublic(updateRankingDTO.isPublic());
    rankingRepository.save(oldRanking);
  }

  /**
   * Checks if the current authenticated user id matches the user id for service operations.
   * @param userId the id of the user to be checked
   * @throws AccessDeniedException if the current authenticated user id does not match
   */
  private void checkPermissions(Long userId) throws AccessDeniedException {
    UserDTO authUser = authService.getAuthenticatedUser();
    if (!authUser.getId().equals(userId)) {
      throw new AccessDeniedException("You do not have permission to view this ranking");
    }
  }
}
