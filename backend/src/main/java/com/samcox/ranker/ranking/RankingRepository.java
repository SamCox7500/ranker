package com.samcox.ranker.ranking;

import com.samcox.ranker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link Ranking} entities.
 * <p>This interface extends {@link JpaRepository} to provide standard methods like
 * {@code save}, {@code findById}, {@code delete}, and more.
 * It also includes a custom method for finding an optional list of rankings for a specific user.
 * It also includes a method for deleting all rankings belonging to a user.</p>
 *
 * @see User
 */
@Repository
public interface RankingRepository extends JpaRepository<Ranking, Long> {
  /**
   * Deletes all ranking belonging to the user.
   * @param userId the id of the user which will have all rankings belonging to them deleted.
   */
  void deleteByUserId(Long userId);

  /**
   * Returns a list of all rankings belonging to a user
   * @param user the user to search for rankings belonging to them
   * @return an {@link Optional} list of all rankings belonging to the {@link User}.
   * Empty if they have no rankings or the user does not exist in the database.
   */
  Optional<List<Ranking>> findByUser(User user);

  /**
   * Returns a numbered ranking by id and user.
   * @param id the id of the user
   * @param user the user to whom the numbered ranking belongs
   * @return an optional numbered ranking. Will be empty if no numbered ranking exists with that id and user.
   */
  Optional<Ranking> findByIdAndUser(Long id, User user);
}
