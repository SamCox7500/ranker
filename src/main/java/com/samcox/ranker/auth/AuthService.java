package com.samcox.ranker.auth;

import com.samcox.ranker.user.*;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;

/**
 * Authentication service for performing authentication related actions.
 */
@Service
public class AuthService {
  /**
   * Repository for accessing users.
   */
  private final UserRepository userRepository;

  /**
   * Constructor for authentication service.
   * @param userRepository repository for accessing users
   */
  public AuthService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Returns the current authenticated user.
   * @return the current authenticated user as a DTO
   */
  public UserDTO getAuthenticatedUser() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth == null || !auth.isAuthenticated()) {
      throw new RuntimeException("User is not authenticated");
    }
    String username = auth.getName();
    User user = userRepository.findByUsername(username)
      .orElseThrow(() -> new UserNotFoundException("Username does not exist"));
    return UserDTOMapper.toUserDTO(user);
  }

  /**
   * Returns if the current user is authenticated
   * @return {@code true } if they are authenticated, {@code false} otherwise
   */
  public boolean isAuthenticated() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    //return !(auth == null || !auth.isAuthenticated());
    return auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken);
  }
}
