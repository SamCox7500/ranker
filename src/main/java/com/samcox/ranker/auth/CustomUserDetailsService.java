package com.samcox.ranker.auth;

import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

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
