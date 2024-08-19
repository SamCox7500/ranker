package com.samcox.ranker;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.ranking.NumberedRankingRepository;
import com.samcox.ranker.ranking.NumberedRankingService;
import com.samcox.ranker.user.UserRepository;
import com.samcox.ranker.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AppConfig {
  /*
  @Bean
  public Validator validator() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    return factory.getValidator();
  }
   */
  @Bean
  public UserService userService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    return new UserService(userRepository, passwordEncoder);
  }
  @Bean
  public NumberedRankingService numberedRankingService(NumberedRankingRepository rankingRepository, UserService userService) {
    return new NumberedRankingService(rankingRepository, userService);
  }
  @Bean
  public AuthService authService(UserService userService) {
    return new AuthService(userService);
  }
}
