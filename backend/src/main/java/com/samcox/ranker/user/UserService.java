package com.samcox.ranker.user;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.ranking.RankingRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Service class for handling business logic related to {@link User} operations.
 *
 * <p>This class provides functionality for creating users, retrieving users by ID or username,
 * updating their passwords, and deleting users.
 * It enforces authorization checks to ensure that users can only access or modify their own data</p>
 *
 * <p>Password handling is done securely using a {@link PasswordEncoder}, and authorization is managed
 * through {@link AuthService}.</p>
 *
 * <p>Marked as {@code @Validated} to support validation on method arguments.</p>
 *
 * @see User
 * @see UserCredentials
 * @see AuthService
 * @see PasswordEncoder
 */
@Validated
@Service
public class UserService {

  /**
   * The repository for accessing user data
   */
  private final UserRepository userRepository;
  /**
   * The repository for accessing user rankings
   */
  private final RankingRepository rankingRepository;

  /**
   * The encoder for encrypting user passwords
   */
  private final PasswordEncoder passwordEncoder;

  /**
   * The service to check if the current user is authenticated
   */
  private final AuthService authService;


  /**
   * Constructs a new {@code UserService} with the required dependencies.
   *
   * @param userRepository the repository for accessing user data
   * @param rankingRepository the depository for accessing user rankings
   * @param passwordEncoder the encoder for encrypting user passwords
   * @param authService the authentication service to check if the current user is authenticated
   */
  public UserService(UserRepository userRepository, RankingRepository rankingRepository, PasswordEncoder passwordEncoder, AuthService authService) {
    this.userRepository = userRepository;
    this.rankingRepository = rankingRepository;
    this.passwordEncoder = passwordEncoder;
    this.authService = authService;
  }

  /**
   * Retrieves all users from the database
   * <p>Note: Not currently in use as it should only be used by admins. Administration is not yet established.</p>
   * @return a list of all users
   */
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  /**
   * Retrieves a user by their ID, ensuring the current user is authorized.
   *
   * @param id the ID of the user to retrieve
   * @return the matching {@link User}
   * @throws AccessDeniedException if the caller is not authorized to access this user
   */
  public User getUserByID(Long id) throws AccessDeniedException {
    checkAuthorized(id);
    return getUserFromRepoById(id);
  }

  /**
   * Retrieves a user by their username, ensuring the current user is authorized
   * @param username the username of the user to retrieve
   * @return the matching {@link User}
   * @throws AccessDeniedException if the caller is not authorized to access this user
   */
  public User getUserByUsername(String username) throws AccessDeniedException {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    checkAuthorized(user.getId());
    return user;
  }

  /**
   * Creates a new user with the provided credentials
   *
   * <p>Fails if a user is already logged in or if the username already exists.</p>
   *
   * @param userCredentials the credentials of the new user
   * @throws UsernameExistsException if the username is already taken
   * @throws UserAlreadyLoggedInException if a user is already authenticated
   */
  public void createUser(@Valid UserCredentials userCredentials) {
    if (userRepository.findByUsername(userCredentials.getUsername()).isPresent()) {
      throw new UsernameExistsException("Username is already taken");
    }
    if (authService.isAuthenticated()) {
      throw new UserAlreadyLoggedInException("Cannot create new user when already logged in");
    }
    User user = new User();
    user.setUsername(userCredentials.getUsername());
    user.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
    user.setRole("USER"); //TODO: allow custom role assignment
    userRepository.save(user);
  }

  /**
   * Changes the password of a user after validating their identity and authorization.
   *
   * @param id the ID of the user
   * @param userCredentials the credentials including the new password
   * @throws AccessDeniedException if the caller is not authorized
   * @throws UserNotFoundException if the user does not exist
   */
  //todo change name
  public void changePassword(Long id, @Valid UserCredentials userCredentials) throws AccessDeniedException {
    if (id == null) {
      throw new UserNotFoundException("User not found because ID is null");
    }
    checkAuthorized(id);
    User user = getUserFromRepoById(id);
    if (!user.getUsername().equals(userCredentials.getUsername())) {
      throw new RuntimeException("ID provided does not belong to that user");
    }
    user.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
    userRepository.save(user);
  }

  /**
   * Deletes a user by their ID after checking for authorization
   * <p>If the user has rankings, they are deleted before the user is removed.</p>
   *
   * @param id the ID of the user to delete
   * @throws AccessDeniedException if the caller is not authorized
   */
  @Transactional
  public void deleteUser(Long id) throws AccessDeniedException {
    checkAuthorized(id);
    User user = getUserFromRepoById(id);

    //Check user has no rankings
    //If user has rankings, then delete them all
    if(rankingRepository.findByUser(user).isPresent()) {
      rankingRepository.deleteByUserId(user.getId());
    }
    userRepository.delete(user);
  }

  /**
   * Helper method to retrieve a user from the repository by ID.
   * @param id the id of the user
   * @return the {@link User} if found
   * @throws AccessDeniedException if the user is not authorized to retrieve that user's data
   */
  private User getUserFromRepoById(Long id) throws AccessDeniedException {
    checkAuthorized(id);
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID "
      + id + " not found"));
  }

  /**
   * Checks if the currently authenticated user is authorized to act on the given user ID.
   * @param userId the id of the user to authorize
   * @throws AccessDeniedException if the current user does not match the given ID
   */
  public void checkAuthorized(Long userId) throws AccessDeniedException {
    if (!authService.getAuthenticatedUser().getId().equals(userId)) {
      throw new AccessDeniedException("User is not authorized");
    }
  }
}
