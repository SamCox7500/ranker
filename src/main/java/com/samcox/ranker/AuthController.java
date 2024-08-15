package com.samcox.ranker;

import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class AuthController {

  //private final SecurityContextRepository securityContextRepository = new HttpSessionSecurityContextRepository();
  private final AuthenticationManager authenticationManager;
  private final SecurityContextRepository securityContextRepository;
  private final UserService userService;
  public AuthController(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository, UserService userService) {
    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
    this.userService = userService;
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
  @GetMapping("/authuser")
  public UserDTO getAuthUser() {
    Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    String username = currentAuth.getName();
    return userService.getUserByUsername(username);
  }
}
