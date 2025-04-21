package com.samcox.ranker.auth;

import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserDTOMapper;
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

/**
 * Controller for handling authentication operations.
 */
@RestController
public class AuthController {

  /**
   * Manages current authentication
   */
  private final AuthenticationManager authenticationManager;
  /**
   * Repository for accessing current security context.
   */
  private final SecurityContextRepository securityContextRepository;
  //private final UserService userService;
  /**
   * Service for performing authentication and authorisation related operations.
   */
  private final AuthService authService;

  /**
   * Constructor for auth controller.
   * @param authenticationManager manages current authentication
   * @param securityContextRepository provides access to security context
   * @param authService service for performing authentication related operations
   */
  public AuthController(AuthenticationManager authenticationManager, SecurityContextRepository securityContextRepository, AuthService authService) {
    this.authenticationManager = authenticationManager;
    this.securityContextRepository = securityContextRepository;
    //this.userService = userService;
    this.authService = authService;
  }

  /**
   * Authenticates a user using their credentials.
   * @param userCredentials username and password for authentication
   * @param request request for authentication
   * @param response response from request for authentication
   * @return the response entity representing the response from the request for authentication
   */
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

  /**
   * Returns the current authenticated user.
   * @return the current authenticated user as DTO
   */
  @GetMapping("/authuser")
  public UserDTO getAuthUser() {
    //Authentication currentAuth = SecurityContextHolder.getContext().getAuthentication();
    //String username = currentAuth.getName();
    //return UserDTOMapper.toUserDTO(userService.getUserByUsername(username));
    return authService.getAuthenticatedUser();
  }
}
