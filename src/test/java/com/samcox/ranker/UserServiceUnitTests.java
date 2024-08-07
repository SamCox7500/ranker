package com.samcox.ranker;

import jakarta.validation.*;
import org.hibernate.mapping.Collection;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class UserServiceUnitTests {

  @Mock
  private UserRepository userRepository;
  @Mock
  private PasswordEncoder passwordEncoder;
  @Mock
  private Validator validator;
  @Mock
  private UserDTOMapper userDTOMapper;

  @InjectMocks
  private UserService userService;

  private User user;
  private UserDTO userDTO;
  private UserCredentials userCredentials;
  @BeforeEach
  public void setUp() {
    user = new User("testuser", "Validpassword1!", "USER");
    userDTO = new UserDTO();
    userDTO.setId(user.getId());
    userDTO.setUsername("testuser");
    userCredentials = new UserCredentials();
    userCredentials.setUsername("testuser");
    userCredentials.setPassword("Validpassword1!");
    //todo add role

    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  @Test
  public void testGetAllUsers() {
    when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
    List<UserDTO> result = userService.getAllUsers();

    assertThat(result).hasSize(1);
    assertThat(result.get(0)).isEqualTo(userDTO);
    verify(userRepository, times(1)).findAll();
  }
  @Test
  public void testGetUserById() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    UserDTO result = userService.getUserByID(1L);

    assertThat(result).isEqualTo(userDTO);
    verify(userRepository, times(1)).findById(1L);
  }
  @Test
  public void testGetUserByUsername() {
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
    UserDTO result = userService.getUserByUsername("testuser");

    assertThat(result).isEqualTo(userDTO);
    verify(userRepository, times(1)).findByUsername("testuser");
  }
  @Test
  public void testCreateUser_Success() {
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
    when(passwordEncoder.encode("Validpassword1!")).thenReturn("encodedpassword");
    when(passwordEncoder.matches("Validpassword1!", "encodedpassword")).thenReturn(true);

    userService.createUser(userCredentials);

    verify(userRepository, times(1)).save(any(User.class));

    ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    verify(userRepository).save(userArgumentCaptor.capture());
    User savedUser = userArgumentCaptor.getValue();

    assertThat(savedUser.getUsername()).isEqualTo("testuser");
    assertThat(passwordEncoder.matches("Validpassword1!", savedUser.getPassword())).isTrue();
  }
  @Test
  public void testCreateUser_UsernameAlreadyExists() {
    when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

    assertThrows(UsernameExistsException.class, () -> {
      userService.createUser(userCredentials);
    });

    verify(userRepository, never()).save(any(User.class));
  }
  @Test
  public void testCreateUser_InvalidUsername() {
    UserCredentials invalidUsernameCredentials = new UserCredentials();
    userCredentials.setUsername("usernametoolonggggggggggggggggg");
    userCredentials.setPassword("Validpassword1!");

    when(userRepository.findByUsername("usernametoolonggggggggggggggggg")).thenReturn(Optional.empty());

    Set<ConstraintViolation<UserCredentials>> violations = validator.validate(invalidUsernameCredentials);
    assertThat(violations).isNotEmpty();
  }
  @Test
  public void testCreateUser_InvalidPassword() {
    UserCredentials invalidPasswordCredentials = new UserCredentials();
    userCredentials.setUsername("newuser");
    userCredentials.setPassword("Invalidpassword");

    Set<ConstraintViolation<UserCredentials>> violations = validator.validate(invalidPasswordCredentials);
    assertThat(violations).isNotEmpty();
  }
  @Test
  public void testChangePassword_Success() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(passwordEncoder.encode("Password1!")).thenReturn("encodedPassword");

    userService.changePassword(1L, userCredentials);

    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).findById(1L);
  }
  @Test
  public void testChangePassword_UsernameMismatch() {
    User unexpectedUser = new User("differentuser", "Validpassword1!", "USER");
    when(userRepository.findById(1L)).thenReturn(Optional.of(unexpectedUser));

    assertThrows(RuntimeException.class, () -> {
      userService.changePassword(1L, userCredentials);
    });

    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, never()).save(any(User.class));
  }
  @Test
  public void testChangePassword_UserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(UserNotFoundException.class, () -> {
      userService.changePassword(1L, userCredentials);
    });

    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, never()).save(any(User.class));
  }
  @Test
  public void testChangePassword_InvalidPassword() {
    UserCredentials invalidPasswordCredentials = new UserCredentials();
    userCredentials.setUsername("existinguser");
    userCredentials.setPassword("Invalidpassword");

    Set<ConstraintViolation<UserCredentials>> violations = validator.validate(invalidPasswordCredentials);
    assertThat(violations).isNotEmpty();
  }
  @Test
  public void testDeleteUser_Success() {
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    userService.deleteUser(1L);
    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).delete(user);
  }
  @Test
  public void testDeleteUser_UserNotFound() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());
    assertThrows(UserNotFoundException.class, () -> {
      userService.deleteUser(1L);
    });
    verify(userRepository, times(1)).findById(1L);
    verify(userRepository, never()).delete(user);
  }
}
