package com.samcox.ranker;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {
  private CustomUserDetailsService customUserDetailsService;
  @PostMapping("/register")
  public String register(@RequestBody RegistrationRequest request) {
    customUserDetailsService.registerUser(request.getUsername(), request.getPassword());
    return "User registered successfully";
  }
}
