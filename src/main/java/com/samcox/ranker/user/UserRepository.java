package com.samcox.ranker.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link User} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide standard methods like
 * {@code save}, {@code findById}, {@code delete}, and more.
 * It also includes a custom method for finding a user by their username.</p>
 *
 * <p>Spring Data JPA automatically generates the implementation at runtime</p>
 *
 * @see User
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  /**
   * Finds the user by their username.
   * @param username the username to search for
   * @return an {@link Optional} containing the matching {@link User}, or empty if no match is found
   */
  Optional<User> findByUsername(String username);
}
