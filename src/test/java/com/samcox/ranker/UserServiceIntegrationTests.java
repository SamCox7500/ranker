package com.samcox.ranker;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserServiceIntegrationTests {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private UserService userService;

  private User user;

  @BeforeEach
  public void setUp() {
    user = new User("testuser", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(user);
  }
  @Test
  public void testGetUserByID_Success() {
    UserDTO userDTO = userService.getUserByID(user.getId());
    assertNotNull(userDTO);
    assertEquals(user.getUsername(), userDTO.getUsername());
  }
  @Test
  public void testGetUserByID_UserNotFound() {
    assertThrows(UserNotFoundException.class, () -> userService.getUserByID(999L));
  }
  @Test
  public void testGetUserByUsername_Success() {
    UserDTO userDTO = userService.getUserByUsername("testuser");
    assertNotNull(userDTO);
    assertEquals(user.getUsername(), userDTO.getUsername());
  }
  @Test
  public void testGetUserByUsername_UserNotFound() {
    assertThrows(UserNotFoundException.class, () -> userService.getUserByUsername("nonexistentuser"));
  }
  @Test
  public void testCreateUser_Success() {
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
  public void testCreateUser_UsernameAlreadyExists() {
    UserCredentials existingUsernameCredentials = new UserCredentials();
    existingUsernameCredentials.setUsername("testuser");
    existingUsernameCredentials.setPassword("Validpassword1!");

    assertThrows(UsernameExistsException.class, () -> userService.createUser(existingUsernameCredentials));
  }
  @Test
  public void testChangePassword_Success() {
    UserCredentials newCredentials = new UserCredentials();
    newCredentials.setUsername("testuser");
    newCredentials.setPassword("NewValidPassword1!");

    userService.changePassword(user.getId(), newCredentials);

    User updatedUser = userRepository.findById(user.getId()).orElse(null);
    assertNotNull(updatedUser);
    assertTrue(passwordEncoder.matches("NewValidPassword1!", updatedUser.getPassword()));
  }
  @Test
  public void testChangePassword_IdMismatch() {
    UserCredentials newCredentials = new UserCredentials();
    newCredentials.setUsername("wronguser");
    newCredentials.setPassword("NewValidPassword1!");

    assertThrows(RuntimeException.class, () -> userService.changePassword(user.getId(), newCredentials));
  }
  @Test
  public void testDeleteUser_Success() {
    userService.deleteUser(user.getId());

    Optional<User> deletedUser = userRepository.findById(user.getId());
    assertTrue(deletedUser.isEmpty());
  }
  @Test
  public void testDeleteUser_UserNotFound() {
    assertThrows(UserNotFoundException.class, () -> userService.deleteUser(999L));
  }
}
