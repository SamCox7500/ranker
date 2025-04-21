package com.samcox.ranker.user;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * REST controller for handling user-related operations.
 *
 * <p>This controller provides endpoints to perform CRUD operations on users,
 * including creating a user, retrieving a user by ID, updating a user's password,
 * and deleting a user.</p>
 *
 * <p>Cross-origin requests are allowed from {@code http://localhost:4200} to support
 * communication with a frontend application running on that origin (e.g. Angular).</p>
 *
 * <p>Note: Currently, only one user can be retrieved at a time. Listing all users
 * is commented out for future implementation.</p>
 *
 * @see UserService
 * @see UserDTO
 * @see UserCredentials
 */
@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {

  /**
   * The service layer the controller uses to perform user operations.
   */
  private final UserService userService;

  /**
   * Constructs a new {@code UserController} with the specified {@link UserService}.
   * @param userService The service layer used to perform user operations
   */
  public UserController(UserService userService) {
    this.userService = userService;
  }

  /**
   *  Retrieves a single user by their id
   *
   * @param id the ID of the user to retrieve
   * @return a {@link UserDTO} representing the user
   * @throws AccessDeniedException if the caller is not authorized to access this user
   */
  @GetMapping("/users/{id}")
  public UserDTO getUser(@PathVariable("id") Long id) throws AccessDeniedException {
    return UserDTOMapper.toUserDTO(userService.getUserByID(id));
  }

  /**
   * Creates a new user with the provided credentials.
   * @param userCredentials the credentials (username, password) for the new user.
   */
  @PostMapping("/users")
  public void createUser(@Valid @RequestBody UserCredentials userCredentials) {
    userService.createUser(userCredentials);
  }

  /**
   * Updates an existing user's password
   * <p>Note: Only password changes are currently supported. Other field updates should
   * be added in the future</p>
   *
   * @param id the ID of the user to update
   * @param userCredentials the new credentials, including the new password
   * @throws AccessDeniedException if the caller is not authorized to update this user
   */
  @PutMapping("/users/{id}")
  public void updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserCredentials userCredentials) throws AccessDeniedException {
    userService.changePassword(id, userCredentials);
  }

  /**
   * Deletes a user by their ID.
   *
   * @param id the ID of the user to delete
   * @throws AccessDeniedException if the caller is not authorized to delete this user
   */
  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable("id") Long id) throws AccessDeniedException {
    userService.deleteUser(id);
  }
  /*
  //@GetMapping("/users")
  //public List<UserDTO> getUsers() {
    //return UserDTOMapper.toUserDTOs(userService.getAllUsers());
    }
  */
}
