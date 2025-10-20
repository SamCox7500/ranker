package com.samcox.ranker.sharedranking;

import com.samcox.ranker.numberedranking.NumberedRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link SharedRanking} entities.
 * <p>This interface extends {@link JpaRepository} to provide standard methods like
 * {@code save}, {@code findById}, {@code delete}, and more.
 * Includes custom methods for deleting a numbered ranking by user, or id and user.
 * Also includes methods for finding a shared ranking by share token.
 */
@Repository
public interface SharedRankingRepository extends JpaRepository<SharedRanking, Long> {

  /**
   * Returns a shared ranking by its share token.
   * @param shareToken unique string used to identify a shared ranking
   * @return Optional containing a shared ranking, or empty if a shared ranking does not exist for that token
   */
  Optional<SharedRanking> findByShareToken(String shareToken);
}
