package com.samcox.ranker;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
  private final UserService userService;
  public UserController(UserService userService) {
    this.userService = userService;
  }
  @GetMapping("/users")
  public List<UserDTO> getUsers() {
    return userService.getAllUsers();
  }
  @GetMapping("/users/{id}")
  public UserDTO getUser(@PathVariable("id") Long id) {
     return userService.getUserByID(id);
  }
  @PostMapping("/users")
  public void createUser(@Valid @RequestBody UserCredentials userCredentials) {
    userService.createUser(userCredentials);
  }
  //todo move password changing logic somewhere else
  //todo have update other fields by here
  @PutMapping("/users/{id}")
  public void updateUser(@PathVariable("id") Long id, @Valid @RequestBody UserCredentials userCredentials) {
    userService.changePassword(id, userCredentials);
  }
  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
  }
}
