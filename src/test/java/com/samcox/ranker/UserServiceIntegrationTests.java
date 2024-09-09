package com.samcox.ranker;


import com.samcox.ranker.ranking.NumberedRanking;
import com.samcox.ranker.ranking.NumberedRankingRepository;
import com.samcox.ranker.ranking.Ranking;
import com.samcox.ranker.ranking.RankingNotFoundException;
import com.samcox.ranker.user.*;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional

public class UserServiceIntegrationTests {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private NumberedRankingRepository numberedRankingRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserService userService;

  @Autowired
  private AuthenticationManager authenticationManager;

  /*
  public UserServiceIntegrationTests(
    UserRepository userRepository,
    PasswordEncoder passwordEncoder,
    UserService userService,
    AuthenticationManager authenticationManager
  ) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.userService = userService;
    this.authenticationManager = authenticationManager;
  }
   */
  private User testUser;
  private User testUser1;

  @BeforeEach
  public void setUp() {
    testUser = new User("testuser", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser);

    testUser1 = new User("testuser1", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser1);

    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("testuser");
    userCredentials.setPassword("Validpassword1!");

    //Authenticating user for testing methods that require authentication
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      userCredentials.getUsername(), userCredentials.getPassword());
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
  }
  @Test
  public void testGetUserByID_Success() throws AccessDeniedException {
    User user = userService.getUserByID(testUser.getId());
    assertNotNull(user);
    assertEquals(testUser.getUsername(), user.getUsername());
  }
  @Test
  public void testGetUserByID_UserNotAuthorized() {
    //The user to get has a different id than the currently authenticated user.
    //So no access is granted
    assertThrows(AccessDeniedException.class,() -> userService.getUserByID(999L));
  }
  @Test
  public void testGetUserByUsername_Success() throws AccessDeniedException {
    User user = userService.getUserByUsername("testuser");
    assertNotNull(user);
    assertEquals(testUser.getUsername(), user.getUsername());
  }
  @Test
  public void testGetUserByUsername_UserNotFound() {
    assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("nonexistentuser"));
  }
  @Test
  public void testGetUserByUsername_NotAuthorized() {
    assertThrows(AccessDeniedException.class, () -> userService.getUserByUsername("testuser1"));
  }

  @Test
  public void testCreateUser_Success() {
    //Logout the test user as a user should not be created when a user is already authenticated.
    SecurityContextHolder.clearContext();

    UserCredentials newUserCredentials = new UserCredentials();
    newUserCredentials.setUsername("newuser");
    newUserCredentials.setPassword("Validpassword1!");

    userService.createUser(newUserCredentials);

    User createdUser = userRepository.findByUsername("newuser").orElse(null);
    assertNotNull(createdUser);
    assertEquals("newuser", createdUser.getUsername());
    assertTrue(passwordEncoder.matches("Validpassword1!", createdUser.getPassword()));
  }
  @Test
  public void testCreateUser_UserAlreadyAuthenticated() {

    UserCredentials newUserCredentials = new UserCredentials();
    newUserCredentials.setUsername("newuser");
    newUserCredentials.setPassword("Validpassword1!");

    //Runtime exception as user is already logged in and cannot create a new user
    assertThrows(RuntimeException.class, () -> userService.createUser(newUserCredentials));
  }

  @Test
  public void testCreateUser_UsernameAlreadyExists() {
    UserCredentials existingUsernameCredentials = new UserCredentials();
    existingUsernameCredentials.setUsername("testuser");
    existingUsernameCredentials.setPassword("Validpassword1!");

    assertThrows(UsernameExistsException.class, () -> userService.createUser(existingUsernameCredentials));
  }
  @Test
  public void testCreateUser_BlankUsername() {
    UserCredentials invalidUsernameCredentials = new UserCredentials();

    //Username is blank which violates constraints
    invalidUsernameCredentials.setUsername("");
    invalidUsernameCredentials.setPassword("Validpassword1!");

    assertThrows(ConstraintViolationException.class, () -> userService.createUser(invalidUsernameCredentials));
  }
  @Test
  public void testCreateUser_UsernameTooLong() {
    UserCredentials invalidUsernameCredentials = new UserCredentials();

    //Username is more than 30 chars which violates constraints

    invalidUsernameCredentials.setUsername("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    invalidUsernameCredentials.setPassword("Validpassword1!");

    assertThrows(ConstraintViolationException.class, () -> userService.createUser(invalidUsernameCredentials));
  }
  @Test
  public void testCreateUser_InvalidPasswordPattern() {
    UserCredentials invalidPasswordCredentials = new UserCredentials();

    //Password does not match required pattern. In this case missing special char.
    //This violates constraints

    invalidPasswordCredentials.setUsername("validusername");
    invalidPasswordCredentials.setPassword("Invalidpassword1");

    assertThrows(ConstraintViolationException.class, () -> userService.createUser(invalidPasswordCredentials));
  }


  @Test
  public void testChangePassword_Success() throws AccessDeniedException {
    UserCredentials newCredentials = new UserCredentials();
    newCredentials.setUsername("testuser");
    newCredentials.setPassword("NewValidPassword1!");

    userService.changePassword(testUser.getId(), newCredentials);

    User updatedUser = userRepository.findById(testUser.getId()).orElse(null);
    assertNotNull(updatedUser);
    assertTrue(passwordEncoder.matches("NewValidPassword1!", updatedUser.getPassword()));
  }

  @Test
  public void testChangePassword_IdMismatch() {
    UserCredentials newCredentials = new UserCredentials();
    newCredentials.setUsername("wronguser");
    newCredentials.setPassword("NewValidPassword1!");

    assertThrows(RuntimeException.class, () -> userService.changePassword(testUser.getId(), newCredentials));
  }
  @Test
  public void testChangePassword_NotAuthorized() {
    UserCredentials newCredentials = new UserCredentials();
    newCredentials.setUsername("testuser");
    newCredentials.setPassword("NewValidPassword1!");

    //Authenticated user trying to change the credentials of a different user
    assertThrows(AccessDeniedException.class, () -> userService.changePassword(testUser1.getId(), newCredentials));
  }

  @Test
  public void testDeleteUser_Success() throws AccessDeniedException {
    userService.deleteUser(testUser.getId());

    Optional<User> deletedUser = userRepository.findById(testUser.getId());
    assertTrue(deletedUser.isEmpty());
  }
  @Test
  public void testDeleteUser_NotAuthorized() {
    assertThrows(AccessDeniedException.class, () -> userService.deleteUser(999L));
  }
  @Test
  public void testDeleteUserWithRankings_Success() throws AccessDeniedException {
    SecurityContextHolder.clearContext();

    User userWithRankings = new User("userwithrankings", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(userWithRankings);
    UserCredentials userWithRankingsCredentials = new UserCredentials();
    userWithRankingsCredentials.setUsername("userwithrankings");
    userWithRankingsCredentials.setPassword("Validpassword1!");

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      userWithRankingsCredentials.getUsername(), userWithRankingsCredentials.getPassword());
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);



    //Testing that if a user has rankings, and the user is deleted, the rankings are as well
    NumberedRanking numberedRanking = new NumberedRanking();
    numberedRanking.setUser(userWithRankings);
    numberedRanking.setTitle("Numbered ranking for test user");
    numberedRanking.setDescription("This is a desc of a ranking");
    numberedRankingRepository.save(numberedRanking);

    long numRankId = numberedRanking.getId();
    long deleteUserId = userWithRankings.getId();

    userService.deleteUser(userWithRankings.getId());

    //Check the numbered ranking belonging to the user has been deleted
    Optional<NumberedRanking> numberedRankingOptional = numberedRankingRepository.findById(numRankId);
    assertTrue(numberedRankingOptional.isEmpty());

    //Check the user has been deleted
    Optional<User> deletedUser = userRepository.findById(deleteUserId);
    assertTrue(deletedUser.isEmpty());

  }
  @Test
  public void testCheckAuthorized_Success() throws AccessDeniedException {
    //Checking authenticated user id matches the id to be checked for authorisation
    //No exception should be thrown
    userService.checkAuthorized(testUser.getId());
  }
  @Test
  public void testCheckAuthorized_NotAuthorized() throws AccessDeniedException {
    //Id to be checked does not match authenticated user, therefore exception should be thrown
    assertThrows(AccessDeniedException.class, () -> userService.checkAuthorized(testUser1.getId()));
  }
}
