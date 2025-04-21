package com.samcox.ranker;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigurationPackage;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class AuthServiceIntegrationTests {

  @Autowired
  AuthService authService;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  UserRepository userRepository;

  @Autowired
  AuthenticationManager authenticationManager;

  @Test
  public void testIsAuthenticated() {

    User testUser = new User("testuser", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser);

    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("testuser");
    userCredentials.setPassword("Validpassword1!");

    //Authenticating user for testing methods that require authentication
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      userCredentials.getUsername(), userCredentials.getPassword());
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.clearContext();
    SecurityContextHolder.setContext(context);

    //User is logged in so should be true
    assertTrue(authService.isAuthenticated());

    SecurityContextHolder.clearContext();
    //No user is authenticated so now should be false
    assertFalse(authService.isAuthenticated());
  }
  @Test
  public void testGetAuthenticatedUser() {
    User testUser = new User("testuser", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser);

    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("testuser");
    userCredentials.setPassword("Validpassword1!");

    //Authenticating user for testing methods that require authentication
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      userCredentials.getUsername(), userCredentials.getPassword());
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.clearContext();
    SecurityContextHolder.setContext(context);

    assertEquals(userCredentials.getUsername(), authService.getAuthenticatedUser().getUsername());

    SecurityContextHolder.clearContext();
    assertThrows(RuntimeException.class, () -> authService.getAuthenticatedUser());
  }

}
