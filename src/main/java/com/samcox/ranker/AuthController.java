package com.samcox.ranker;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

  private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
  private final AuthenticationManager authenticationManager;
  public AuthController(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }
  @PostMapping("/login")
  public ResponseEntity<Map<String, String>> login(@RequestBody UserCredentials userCredentials, HttpServletRequest request, HttpServletResponse response) {
    Map<String, String> loginResponse = new HashMap<>();
    SecurityContextHolder.clearContext();
    try {
      UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
        userCredentials.getUsername(), userCredentials.getPassword());
      Authentication authentication = authenticationManager.authenticate(authToken);
      SecurityContext context = SecurityContextHolder.createEmptyContext();
      context.setAuthentication(authentication);
      SecurityContextHolder.setContext(context);
      securityContextRepository.saveContext(context, request, response);

      loginResponse.put("message", "Login Successful");
      return ResponseEntity.ok(loginResponse);

    } catch (Exception e) {

      loginResponse.put("message", "Login Failed");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(loginResponse);
    }
  }
  //@PostMapping("/logout")
  //public ResponseEntity
  @PostMapping("/testdata")
  public String testLogin(@RequestBody UserCredentials userCredentials) {
    return userCredentials.getUsername() + " " + userCredentials.getPassword();
  }
}
