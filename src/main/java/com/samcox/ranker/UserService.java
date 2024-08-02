package com.samcox.ranker;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

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
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
  }
  public User getUserByUsername(String username) {
    return userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
  }
  public void createUser(UserCredentials userCredentials) {
    if (userRepository.findByUsername(userCredentials.getUsername()).isPresent()) {
      throw new RuntimeException("Username has been taken");
    }
    User user = new User();
    user.setUsername(userCredentials.getUsername());
    user.setPassword(passwordEncoder.encode(userCredentials.getPassword()));
    user.setRole("USER");
    userRepository.save(user);
  }
  public void changePassword(Long id, String password) {
    User user = getUserByID(id);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
  }
  public void deleteUser(Long id) {
    User user = getUserByID(id);
    userRepository.delete(user);
  }
}
