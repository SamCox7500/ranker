package com.samcox.ranker.user;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.ranking.RankingRepository;
import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Validated
@Service
public class UserService {

  private final UserRepository userRepository;
  private final RankingRepository rankingRepository;
  private final PasswordEncoder passwordEncoder;

  private final AuthService authService;

  public UserService(UserRepository userRepository, RankingRepository rankingRepository, PasswordEncoder passwordEncoder, AuthService authService) {
    this.userRepository = userRepository;
    this.rankingRepository = rankingRepository;
    this.passwordEncoder = passwordEncoder;
    this.authService = authService;
  }

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUserByID(Long id) throws AccessDeniedException {
    checkAuthorized(id);
    return getUserFromRepoById(id);
  }

  public User getUserByUsername(String username) throws AccessDeniedException {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
    checkAuthorized(user.getId());
    return user;
  }

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
    user.setRole("USER"); //todo
    userRepository.save(user);
  }

  //todo change name
  public void changePassword(Long id, @Valid UserCredentials userCredentials) throws AccessDeniedException {
    checkAuthorized(id);
    User user = getUserFromRepoById(id);
    if (!user.getUsername().equals(userCredentials.getUsername())) {
      throw new RuntimeException("ID provided does not belong to that user");
    }
    user.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
    userRepository.save(user);
  }

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

  private User getUserFromRepoById(Long id) throws AccessDeniedException {
    checkAuthorized(id);
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID "
      + id + " not found"));
  }
  public void checkAuthorized(Long userId) throws AccessDeniedException {
    if (!authService.getAuthenticatedUser().getId().equals(userId)) {
      throw new AccessDeniedException("User is not authorized");
    }
  }
}
