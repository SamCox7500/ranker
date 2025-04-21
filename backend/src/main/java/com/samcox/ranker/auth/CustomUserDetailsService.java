package com.samcox.ranker.auth;

import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Service for retrieving user details.
 * @see UserDetails
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

  /**
   * Repository for accessing user data
   */
  private UserRepository userRepository;

  /**
   * Constructor for custom user details service
   * @param userRepository repository for accessing users
   */
  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Returns user details used for authentication
   * @param username the username of the user
   * @return {@link UserDetails} used for authentication
   * @throws UsernameNotFoundException if there does not exist a user with the given username
   */
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

    UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
      .username(user.getUsername())
      .password(user.getPassword())
      .roles("USER")
      .build();

    return userDetails;
  }
}
