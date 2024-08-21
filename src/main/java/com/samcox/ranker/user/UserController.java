package com.samcox.ranker.user;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }
  //@GetMapping("/users")
  //public List<UserDTO> getUsers() {
    //return UserDTOMapper.toUserDTOs(userService.getAllUsers());
  //}
  @GetMapping("/users/{id}")
  public UserDTO getUser(@PathVariable("id") Long id) throws AccessDeniedException {
    return UserDTOMapper.toUserDTO(userService.getUserByID(id));
  }
  @PostMapping("/users")
  public void createUser(@Valid @RequestBody UserCredentials userCredentials) {
    userService.createUser(userCredentials);
  }
  //todo move password changing logic somewhere else
  //todo have update other fields by here
  @PutMapping("/users/{id}")
  public void updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserCredentials userCredentials) throws AccessDeniedException {
    userService.changePassword(id, userCredentials);
  }
  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable("id") Long id) throws AccessDeniedException {
    userService.deleteUser(id);
  }
}
