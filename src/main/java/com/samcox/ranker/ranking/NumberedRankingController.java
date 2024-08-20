package com.samcox.ranker.ranking;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
public class NumberedRankingController {

  private final NumberedRankingService numberedRankingService;

  public NumberedRankingController(NumberedRankingService numberedRankingService) {
    this.numberedRankingService = numberedRankingService;
  }
  @GetMapping("/users/{userId}/numberedranrankings")
  public List<NumberedRankingDTO> getAllRankings(@PathVariable("userId") long userId) throws AccessDeniedException {
    return NumberedRankingDTOMapper.toNumberedRankingDTOs(numberedRankingService
      .getAllNumberedRankingsByUser(userId));
  }
  @GetMapping("/users/{userId}/numberedrankings/{rankingId}")
  public NumberedRankingDTO getRanking(@PathVariable("rankingId") long rankingId, @PathVariable("userId") long userId) throws AccessDeniedException {
    return NumberedRankingDTOMapper.toNumberedRankingDTO(numberedRankingService.getNumberedRankingByUserAndId(rankingId, userId));
  }
  //todo should service method have id seperate as well.
  @PostMapping("/users/{userId}/numberedrankings")
  public void createRanking(@PathVariable("userId") long userId, @Valid NumberedRankingDTO rankingDTO) throws AccessDeniedException {
    if (userId != rankingDTO.getUserDTO().getId()) {
      throw new RuntimeException("UserId Mismatch");
    }
    numberedRankingService.createNumberedRanking(rankingDTO);
  }
  @PutMapping("/users/{userId}/numberedrankings/{rankingId}")
  public void updateRanking(@PathVariable("userId") long userId, @Valid NumberedRankingDTO rankingDTO) throws AccessDeniedException {
    if (userId != rankingDTO.getUserDTO().getId()) {
      throw new RuntimeException("UserId Mismatch");
    }
    numberedRankingService.updateNumberedRanking(rankingDTO);
  }
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}")
  public void deleteRanking(@PathVariable("rankingId") long rankingId, @PathVariable("userId") long userId) throws AccessDeniedException {
    numberedRankingService.deleteNumberedRankingByIdAndUser(rankingId, userId);
  }
}
