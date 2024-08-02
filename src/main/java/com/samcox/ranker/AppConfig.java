package com.samcox.ranker;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
  @Bean
  public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return new UserService(userRepository, passwordEncoder);
  }
}
