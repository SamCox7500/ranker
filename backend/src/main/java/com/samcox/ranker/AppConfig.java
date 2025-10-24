package com.samcox.ranker;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.media.MediaListEntryRepository;
import com.samcox.ranker.media.MediaListEntryService;
import com.samcox.ranker.media.MediaListRepository;
import com.samcox.ranker.media.MediaListService;
import com.samcox.ranker.numberedranking.NumberedRankingRepository;
import com.samcox.ranker.numberedranking.NumberedRankingService;
import com.samcox.ranker.ranking.RankingRepository;
import com.samcox.ranker.ranking.RankingService;
import com.samcox.ranker.sharedranking.SharedRankingRepository;
import com.samcox.ranker.sharedranking.SharedRankingService;
import com.samcox.ranker.tmdb.TmdbService;
import com.samcox.ranker.user.UserRepository;
import com.samcox.ranker.user.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AppConfig {
  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }
  @Bean
  public UserService userService(UserRepository userRepository, RankingRepository rankingRepository, PasswordEncoder passwordEncoder, AuthService authService) {
    return new UserService(userRepository, rankingRepository, passwordEncoder, authService);
  }

  @Bean
  public NumberedRankingService numberedRankingService(NumberedRankingRepository rankingRepository, UserService userService, AuthService authService) {
    return new NumberedRankingService(rankingRepository, userService, authService);
  }
  @Bean
  public RankingService rankingService(RankingRepository rankingRepository, UserService userService, AuthService authService) {
    return new RankingService(rankingRepository, userService, authService);
  }
  @Bean
  public AuthService authService(UserRepository userRepository) {
    return new AuthService(userRepository);
  }

  @Bean
  public MediaListService mediaListService(MediaListRepository mediaListRepository, NumberedRankingService numberedRankingService, MediaListEntryService mediaListEntryService, AuthService authService, TmdbService tmdbService) {
    return new MediaListService(mediaListRepository, numberedRankingService, mediaListEntryService, authService, tmdbService);
  }
  @Bean
  public MediaListEntryService mediaListEntryService(MediaListEntryRepository mediaListEntryRepository, AuthService authService) {
    return new MediaListEntryService(mediaListEntryRepository, authService);
  }
  @Bean
  public TmdbService tmdbService(RestTemplate restTemplate) {
    return new TmdbService(restTemplate);
  }

  @Bean
  public SharedRankingService sharedRankingService(SharedRankingRepository sharedRankingRepository, RankingRepository rankingRepository, RankingService rankingService) {
    return new SharedRankingService(sharedRankingRepository, rankingRepository, rankingService);
  }
}
