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
  public List<UserDTO> getAllUsers() {
    return UserDTOMapper.toUserDTOs(userRepository.findAll());
  }
  public UserDTO getUserByID(Long id) {
    User user = getUserFromRepoById(id);
    return UserDTOMapper.toUserDTO(user);
  }
  public UserDTO getUserByUsername(String username) {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UserNotFoundException("User with username "
      + username + " not found"));
    return UserDTOMapper.toUserDTO(user);
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
  /*
  private void validate(UserCredentials userCredentials) {
    Set<ConstraintViolation<UserCredentials>> violations = validator.validate(userCredentials);
    if (!violations.isEmpty()) {
      StringBuilder sb = new StringBuilder();
      for (ConstraintViolation<UserCredentials> constraintViolation: violations) {
        sb.append(constraintViolation.getMessage());
      }
      throw new ConstraintViolationException("Error occured: " + sb, violations);
    }
  }
   */
}
