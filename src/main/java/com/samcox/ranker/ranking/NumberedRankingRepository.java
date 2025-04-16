package com.samcox.ranker.ranking;

import com.samcox.ranker.user.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link NumberedRanking} entities.
 * <p>This interface extends {@link JpaRepository} to provide standard methods like
 * {@code save}, {@code findById}, {@code delete}, and more.
 * Includes custom methods for deleting a numbered ranking by user, or id and user.
 * Also includes methods for finding a numbered ranking by user, id or user and id.
 */
@Repository
public interface NumberedRankingRepository extends JpaRepository<NumberedRanking, Long> {
  /**
   * Returns an optional list of numbered rankings belonging to a user.
   * @param user the user to whom the list of numbered rankings belong
   * @return a list of numbered rankings belonging to a user. Optional as a user may have no numbered rankings.
   */
  Optional<List<NumberedRanking>> findByUser(User user);

  /**
   * Returns a numbered ranking by id.
   * @param id the id of the numbered ranking
   * @return an optional numbered ranking. Will be empty if no numbered ranking exists with that id.
   */
  Optional<NumberedRanking> findById(long id);

  /**
   * Returns a numbered ranking by id and user.
   * @param id the id of the user
   * @param user the user to whom the numbered ranking belongs
   * @return an optional numbered ranking. Will be empty if no numbered ranking exists with that id and user.
   */
  Optional<NumberedRanking> findByIdAndUser(long id, User user);

  /**
   * Deletes all numbered rankings belonging to a user.
   * @param user the user who's numbered ranking are to be deleted
   */
  void deleteByUser(User user);

  /**
   * Deletes a numbered ranking by id and user.
   * @param id the id of the numbered ranking to be deleted
   * @param user the user that owns the numbered ranking
   */
  @Transactional
  void deleteByIdAndUser(long id, User user);
}
