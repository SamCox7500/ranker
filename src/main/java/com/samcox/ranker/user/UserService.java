package com.samcox.ranker.user;

import jakarta.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }
  public User getUserByID(Long id) {
    return getUserFromRepoById(id);
  }
  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username)
      .orElseThrow(() -> new UserNotFoundException("User with username " + username + " not found"));
  }
  public void createUser(@Valid UserCredentials userCredentials) {
    if (userRepository.findByUsername(userCredentials.getUsername()).isPresent()) {
      throw new UsernameExistsException("Username is already taken");
    }
    User user = new User();
    user.setUsername(userCredentials.getUsername());
    user.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
    user.setRole("USER"); //todo
    userRepository.save(user);
  }
  public void changePassword(Long id, @Valid UserCredentials userCredentials) {
    User user = getUserFromRepoById(id);
    if (!user.getUsername().equals(userCredentials.getUsername())) {
      throw new RuntimeException("ID provided does not belong to that user");
    }
    user.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
    userRepository.save(user);
  }
  public void deleteUser(Long id) {
    User user = getUserFromRepoById(id);
    userRepository.delete(user);
  }
  private User getUserFromRepoById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new UserNotFoundException("User with ID "
      + id + " not found"));
  }
}
