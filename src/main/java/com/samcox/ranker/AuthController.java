package com.samcox.ranker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @PostMapping("/login")
  public String login() {

  }

}
