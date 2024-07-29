
package com.samcox.ranker;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
public class LoginController {

  /*
  @GetMapping("/login")
  String login() {
    return "login";
  }
  @GetMapping("/logout")
  String logout() {
    return "test";
  }
   */

  //private final AuthenticationManager authenticationManager;

  /*
  public LoginController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }


   */
  /*
  @PostMapping("/login")
  public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest) {
    Authentication authenticationRequest =
      UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());
    Authentication authenticationResponse =
      this.authenticationManager.authenticate(authenticationRequest);
    //todo
  }
   */
  /*
  public record LoginRequest(String username, String password) {
    //todo
  }
   */
}
