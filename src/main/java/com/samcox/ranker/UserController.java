package com.samcox.ranker;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
  private final UserService userService;
  public UserController(UserService userService) {
    this.userService = userService;
  }
  @GetMapping("/users")
  public List<User> getUsers() {
    return userService.getAllUsers();
  }
  @GetMapping("/users/{id}")
  public User getUser(@PathVariable("id") Long id) {
    return userService.getUserByID(id);
  }
  @PostMapping("/users")
  public void createUser(@RequestBody UserCredentials userCredentials) {
    userService.createUser(userCredentials);
  }
  @PutMapping("/users/{id}")
  public void updateUser(@PathVariable("id") Long id, @RequestBody UserCredentials userCredentials) {
    userService.changePassword(id, userCredentials.getPassword());
  }
  @DeleteMapping("/users/{id}")
  public void deleteUser(@PathVariable("id") Long id) {
    userService.deleteUser(id);
  }
}
