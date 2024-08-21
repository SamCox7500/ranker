package com.samcox.ranker.ranking;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserNotFoundException;
import com.samcox.ranker.user.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.List;

@Validated
@Service
public class NumberedRankingService {
  private final NumberedRankingRepository numberedRankingRepository;
  private final UserService userService;

  private final AuthService authService;

  public NumberedRankingService(NumberedRankingRepository numberedRankingRepository, UserService userService, AuthService authService) {
    this.numberedRankingRepository = numberedRankingRepository;
    this.userService = userService;
    this.authService = authService;
  }
  /*
  public NumberedRanking getNumberedRankingById(long id) {
    return numberedRankingRepository.findById(id)
      .orElseThrow(() -> new RankingNotFoundException("Numbered ranking does not exist with id: " + id));
  }
   */
  public NumberedRanking getNumberedRankingByUserAndId(Long numberedRankingId, Long userId) throws AccessDeniedException {
    checkOwnership(numberedRankingId);
    User user = userService.getUserByID(userId);
    return numberedRankingRepository.findByIdAndUser(numberedRankingId, user)
      .orElseThrow(
        () -> new RankingNotFoundException("Numbered ranking was not found with id: " + numberedRankingId
          + " and user: " + userId)
      );
  }
  /*
  public List<NumberedRanking> getAllNumberedRankings() {
    return numberedRankingRepository.findAll();
  }
   */
  public List<NumberedRanking> getAllNumberedRankingsByUser(Long userId) throws AccessDeniedException {
    User user = userService.getUserByID(userId);
    return numberedRankingRepository.findByUser(user)
      .orElseThrow(() -> new UserNotFoundException("Cannot retrieve numbered rankings because user does not exist"));
  }

  public void createNumberedRanking(@Valid NumberedRankingDTO numberedRankingDTO) throws AccessDeniedException {
    User user = userService.getUserByID(numberedRankingDTO.getUserDTO().getId());
    NumberedRanking numberedRanking = new NumberedRanking();
    numberedRanking.setUser(user);
    numberedRanking.setTitle(numberedRankingDTO.getTitle());
    numberedRanking.setDescription(numberedRankingDTO.getDescription());
    numberedRanking.setPrivate(); //todo
    numberedRanking.setReverseOrder(numberedRankingDTO.isReverseOrder());
    numberedRankingRepository.save(numberedRanking);
  }
  public void updateNumberedRanking(@Valid NumberedRankingDTO newNumberedRanking) throws AccessDeniedException {
    checkOwnership(newNumberedRanking.getId());
    Long id = newNumberedRanking.getId();
    NumberedRanking oldNumberedRanking = numberedRankingRepository.findById(newNumberedRanking.getId())
      .orElseThrow(() -> new RankingNotFoundException("Numbered ranking does not exist with id: " + id));
    oldNumberedRanking.setTitle(newNumberedRanking.getTitle());
    oldNumberedRanking.setDescription(newNumberedRanking.getDescription());
    oldNumberedRanking.setPrivate(); //todo
    oldNumberedRanking.setReverseOrder(newNumberedRanking.isReverseOrder());
    numberedRankingRepository.save(oldNumberedRanking);
  }
  public void deleteNumberedRankingByIdAndUser(Long rankingId, Long userId) throws AccessDeniedException {
    checkOwnership(rankingId);
    if (numberedRankingRepository.findById(rankingId).isEmpty()) {
      throw new RankingNotFoundException("Cannot delete ranking. Ranking does not exist");
    }
    User user = userService.getUserByID(userId);
    numberedRankingRepository.deleteByIdAndUser(rankingId, user);
  }
  public void deleteAllNumberedRankingsByUser(User user) {
    if (numberedRankingRepository.findByUser(user).isEmpty()) {
      throw new RankingNotFoundException("Failed to delete rankings. User has not rankings.");
    }
    numberedRankingRepository.deleteByUser(user);
  }
  public void checkOwnership(Long rankingId) throws AccessDeniedException {
    Long authUserId = authService.getAuthenticatedUser().getId();
    NumberedRanking ranking = numberedRankingRepository.findById(rankingId)
      .orElseThrow(() -> new RankingNotFoundException("Could not check ownership as ranking does not exist with id: " + rankingId));
    if (!ranking.getUser().getId().equals(authUserId)) {
      throw new AccessDeniedException("You do not have permission to access that resource");
    }
  }
}
