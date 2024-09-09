package com.samcox.ranker;

import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRepositoryIntegrationTests {
  @Autowired
  private UserRepository userRepository;

  private User user;

  @BeforeEach
  public void setUp() {
    userRepository.deleteAll();
    user = new User("testuser", "Validpassword1!", "USER");
    userRepository.save(user);
  }

  @Test
  public void testFindByUsername_Success() {
    Optional<User> foundUser = userRepository.findByUsername("testuser");

    assertTrue(foundUser.isPresent());
    assertEquals("testuser", foundUser.get().getUsername());
  }
  /*
  @Test
  public void testFindByUsername_UserNotFound() {
    Optional<User> foundUser = userRepository.findByUsername("nonexistentuser");

    assertTrue(foundUser.isEmpty());
  }
  @Test
  public void testFindById_Success() {
    Optional<User> foundUser = userRepository.findById(user.getId());
    assertThat(foundUser).isPresent();
    assertThat(foundUser.get().getId()).isEqualTo(user.getId());
  }
  @Test
  public void testFindById_UserNotFound() {
    Optional<User> foundUser = userRepository.findById(999L);
    assertThat(foundUser).isNotPresent();
  }
  @Test
  public void testSaveUser_Success() {
    // Test saving a new user to the repository
    User newUser = new User("newuser", "AnotherValidPassword1!", "USER");
    User savedUser = userRepository.save(newUser);

    assertThat(savedUser.getId()).isNotNull(); // Check that the ID was generated
    assertThat(savedUser.getUsername()).isEqualTo("newuser");
    assertThat(savedUser.getPassword()).isEqualTo("AnotherValidPassword1!");
  }
  @Test
  public void testDeleteUser_Success() {
    // Test deleting a user from the repository
    userRepository.delete(user);
    Optional<User> deletedUser = userRepository.findById(user.getId());

    assertThat(deletedUser).isNotPresent();
  }
  @Test
  public void testUpdateUser_Success() {
    // Test updating an existing user in the repository
    user.setPassword("UpdatedPassword1!");
    User updatedUser = userRepository.save(user);

    assertThat(updatedUser.getPassword()).isEqualTo("UpdatedPassword1!");
  }
   */
}
