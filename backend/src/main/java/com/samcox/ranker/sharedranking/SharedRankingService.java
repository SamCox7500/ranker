package com.samcox.ranker.sharedranking;

import com.samcox.ranker.ranking.Ranking;
import com.samcox.ranker.ranking.RankingRepository;
import com.samcox.ranker.ranking.RankingService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.UUID;

/**
 * A service which contains methods that implement business logic for sharing, unsharing and viewing {@link SharedRanking}.
 *
 * <p>Marked as {@code @Validated} to support validation on method arguments.</p>
 *
 * @see SharedRanking
 */
@Validated
@Service
public class SharedRankingService {

  /**
   * The repository for accessing shared ranking data.
   */
  private final SharedRankingRepository sharedRankingRepository;
  /**
   * The repository for accessing ranking data.
   */
  private final RankingRepository rankingRepository;
  /**
   * The service for interacting with rankings.
   */
  private final RankingService rankingService;

  /**
   * Constructor for shared ranking service
   * @param sharedRankingRepository repository for accessing shared ranking data
   * @param rankingRepository repository for accessing rankings directly
   * @param rankingService service for interacting with rankings
   */
  public SharedRankingService(SharedRankingRepository sharedRankingRepository, RankingRepository rankingRepository, RankingService rankingService) {
    this.sharedRankingRepository = sharedRankingRepository;
    this.rankingRepository = rankingRepository;
    this.rankingService = rankingService;
  }

  /**
   * Creates a shared ranking version of a specified ranking.
   * @param rankingId the id of the ranking to be shared
   * @param userId the id of the user that is sharing the ranking
   * @throws AccessDeniedException if the user trying to share the ranking does not own it
   */
  public void shareRanking(Long rankingId, Long userId) throws AccessDeniedException {
    if (isShared(rankingId, userId)) {
      throw new RankingAlreadySharedException("Ranking is already shared");
    }
    Ranking ranking = rankingService.getRankingByIdAndUser(rankingId, userId);
    SharedRanking sharedRanking = new SharedRanking();
    sharedRanking.setShareToken(UUID.randomUUID().toString());
    sharedRanking.setRanking(ranking);

    ranking.setSharedRanking(sharedRanking);
    rankingRepository.save(ranking);
  }

  /**
   * Unshares a ranking. Deleting the shared ranking associated with the ranking.
   * @param rankingId the id of the ranking to be unshared
   * @param userId the id of user unsharing the ranking
   * @throws AccessDeniedException if the user trying to unshare the ranking does not own it
   */
  public void unshareRanking(Long rankingId, Long userId) throws AccessDeniedException {
    Ranking ranking = rankingService.getRankingByIdAndUser(rankingId, userId);
    if (!isShared(rankingId, userId)) {
      //todo response entities
      throw new RankingNotSharedException("Ranking is not currently shared");
    }

    SharedRanking sharedRanking = ranking.getSharedRanking();
    sharedRankingRepository.delete(sharedRanking);
    ranking.setSharedRanking(null);
    rankingRepository.save(ranking);
  }

  /**
   * Returns if a ranking has been shared by its owner or not.
   * @param rankingId the id of the ranking being checked
   * @param userId the id of the user checking if a ranking is shared
   * @return true if the specified ranking has been shared. False otherwise
   * @throws AccessDeniedException if the user checking is not the owner of the ranking
   */
  public boolean isShared(Long rankingId, Long userId) throws AccessDeniedException {
    Ranking ranking = rankingService.getRankingByIdAndUser(rankingId, userId);
    return ranking.getSharedRanking() != null;
  }

  /**
   * Returns the shared ranking associated with the ranking.
   * @param rankingId the id of the ranking that is shared
   * @param userId the id of the user retrieving the shared ranking
   * @return the shared ranking version of the ranking
   * @throws AccessDeniedException if the user trying to retrieve the shared ranking is not the owner of the ranking
   */
  public SharedRanking getSharedRanking(Long rankingId, Long userId) throws AccessDeniedException {
    if (!isShared(rankingId, userId)) {
      throw new RankingNotSharedException("Ranking is not currently shared");
    }
    Ranking ranking = rankingService.getRankingByIdAndUser(rankingId, userId);
    return ranking.getSharedRanking();
  }

  /**
   * Retrieves a ranking that has been shared by another user.
   * <p>Accesses rankings directly by their repository instead of using the ranking service.
   * This bypasses business logic which enforces that only the owner of a ranking can access it.</p>
   * @param shareToken the unique string that allows other users to view your ranking
   * @return the ranking associated with the share token
   */
  public Ranking viewSharedRanking(String shareToken) {
    SharedRanking sharedRanking = sharedRankingRepository.findByShareToken(shareToken)
      .orElseThrow(() -> new InvalidShareTokenException("Invalid share token"));
    return sharedRanking.getRanking();
  }
}
