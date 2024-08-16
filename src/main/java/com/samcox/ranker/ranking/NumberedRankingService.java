package com.samcox.ranker.ranking;

import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserNotFoundException;
import com.samcox.ranker.user.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Validated
@Service
public class NumberedRankingService {
  private final NumberedRankingRepository numberedRankingRepository;
  private final UserService userService;

  public NumberedRankingService(NumberedRankingRepository numberedRankingRepository, UserService userService) {
    this.numberedRankingRepository = numberedRankingRepository;
    this.userService = userService;
  }
  public NumberedRanking getNumberedRankingById(long id) {
    return numberedRankingRepository.findById(id)
      .orElseThrow(() -> new RankingNotFoundException("Numbered ranking does not exist with id: " + id));
  }
  public List<NumberedRanking> getAllNumberedRankings() {
    return numberedRankingRepository.findAll();
  }
  public List<NumberedRanking> getAllNumberedRankingsByUser(User user) {
    return numberedRankingRepository.findByUser(user)
      .orElseThrow(() -> new UserNotFoundException("Cannot retrieve numbered rankings because user does not exist"));
  }

  public void createNumberedRanking(@Valid NumberedRankingDTO numberedRankingDTO) {
    User user = userService.getUserByID(numberedRankingDTO.getUserDTO().getId());
    NumberedRanking numberedRanking = new NumberedRanking();
    numberedRanking.setUser(user);
    numberedRanking.setTitle(numberedRankingDTO.getTitle());
    numberedRanking.setDescription(numberedRankingDTO.getDescription());
    numberedRanking.setPrivate(); //todo
    numberedRanking.setReverseOrder(numberedRankingDTO.isReverseOrder());
    numberedRankingRepository.save(numberedRanking);
  }
  public void updateNumberedRanking(long id, @Valid NumberedRankingDTO newNumberedRanking) {
    NumberedRanking oldNumberedRanking = numberedRankingRepository.findById(id)
      .orElseThrow(() -> new RankingNotFoundException("Numbered ranking does not exist with id: " + id));
    if (oldNumberedRanking.getId() != newNumberedRanking.getId()) {
      throw new RuntimeException("ID mismatch between numbered rankings");
    }
    if (oldNumberedRanking.getUser().getId() != newNumberedRanking.getUserDTO().getId()) {
      throw new RuntimeException("Numbered rankings belong to different users");
    }
    oldNumberedRanking.setTitle(newNumberedRanking.getTitle());
    oldNumberedRanking.setDescription(newNumberedRanking.getDescription());
    oldNumberedRanking.setPrivate(); //todo
    oldNumberedRanking.setReverseOrder(newNumberedRanking.isReverseOrder());
    numberedRankingRepository.save(oldNumberedRanking);
  }
  public void deleteNumberedRankingById(long id) {
    numberedRankingRepository.deleteById(id);
  }
  public void deleteAllNumberedRankingsByUser(User user) {
    numberedRankingRepository.deleteByUser(user);
  }
}
