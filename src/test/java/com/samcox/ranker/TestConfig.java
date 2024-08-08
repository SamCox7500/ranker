package com.samcox.ranker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@TestConfiguration
public class TestConfig {
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
  @Bean
  public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder ) {
    return new UserService(userRepository, passwordEncoder);
  }
  @Bean
  public UserController userController(UserService userService) {
    return new UserController(userService);
  }
  /*
  @Bean
  public MockMvc mockMvc(UserController userController) {
    return MockMvcBuilders.standaloneSetup(userController).build();
  }
   */
  @Bean
  public ObjectMapper objectMapper() {
    return new ObjectMapper();
  }

  @Bean
  public CustomUserDetailsService customUserDetailsService(UserRepository userRepository) {
    return new CustomUserDetailsService(userRepository);
  }
}
