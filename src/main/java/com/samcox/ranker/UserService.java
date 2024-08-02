package com.samcox.ranker;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }
  public List<UserDTO> getAllUsers() {
    return UserDTOMapper.toUserDTOs(userRepository.findAll());
  }
  public UserDTO getUserByID(Long id) {
    User user = getUserFromRepoById(id);
    return UserDTOMapper.toUserDTO(user);
  }
  public UserDTO getUserByUsername(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));
    return UserDTOMapper.toUserDTO(user);
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
    User user = getUserFromRepoById(id);
    user.setPassword(passwordEncoder.encode(password));
    userRepository.save(user);
  }
  public void deleteUser(Long id) {
    User user = getUserFromRepoById(id);
    userRepository.delete(user);
  }
  private User getUserFromRepoById(Long id) {
    return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
  }
}
