package com.samcox.ranker.ranking;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Validated
@Service
public class RankingService {

  private final RankingRepository rankingRepository;
  private final UserService userService;
  private final AuthService authService;

  public RankingService(RankingRepository rankingRepository, UserService userService, AuthService authService) {
    this.rankingRepository = rankingRepository;
    this.userService = userService;
    this.authService = authService;
  }
  public Ranking getRankingByIdAndUser(Long rankingId, Long userId) throws AccessDeniedException {
    checkPermission(userId);
    User user = userService.getUserByID(userId);
    return rankingRepository.findByIdAndUser(rankingId, user)
      .orElseThrow(() -> new RankingNotFoundException("Ranking not found for ranking " + rankingId + " and user " + userId));
  }
  public List<Ranking> getAllRankingsByUser(Long userId) throws AccessDeniedException {
    checkPermission(userId);
    User user = userService.getUserByID(userId);
    return rankingRepository.findByUser(user)
      .orElseThrow(() -> new RankingNotFoundException("No rankings found for user " + userId));
  }
  public void createRanking() {

  }
  public void updateRanking() {

  }
  public void deleteRankingByIdAndUser() {

  }
  public void checkPermission(Long userId) throws AccessDeniedException {
    UserDTO authUser = authService.getAuthenticatedUser();
    if (!authUser.getId().equals(userId)) {
      throw new AccessDeniedException("You do not have permission to view this ranking");
    }
  }
}
