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
  @GetMapping("/users/{userId}/numberedrankings")
  public List<NumberedRankingDTO> getAllRankings(@PathVariable("userId") Long userId) throws AccessDeniedException {
    return NumberedRankingDTOMapper.toNumberedRankingDTOs(numberedRankingService
      .getAllNumberedRankingsByUser(userId));
  }
  @GetMapping("/users/{userId}/numberedrankings/{rankingId}")
  public NumberedRankingDTO getRanking(@PathVariable("rankingId") Long rankingId, @PathVariable("userId") Long userId) throws AccessDeniedException {
    return NumberedRankingDTOMapper.toNumberedRankingDTO(numberedRankingService.getNumberedRankingByUserAndId(rankingId, userId));
  }
  //todo should service method have id seperate as well.
  @PostMapping("/users/{userId}/numberedrankings")
  public void createRanking(@PathVariable("userId") Long userId, @RequestBody @Valid NumberedRankingDTO rankingDTO) throws AccessDeniedException {
    if (!userId.equals(rankingDTO.getUserDTO().getId())) {
      throw new AccessDeniedException("User ID mismatch");
    }
    numberedRankingService.createNumberedRanking(rankingDTO);
  }
  @PutMapping("/users/{userId}/numberedrankings/{rankingId}")
  public void updateRanking(@PathVariable("userId") Long userId, @RequestBody @Valid NumberedRankingDTO rankingDTO) throws AccessDeniedException {
    if (!userId.equals(rankingDTO.getUserDTO().getId())) {
      throw new AccessDeniedException("UserId Mismatch");
    }
    numberedRankingService.updateNumberedRanking(rankingDTO);
  }
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}")
  public void deleteRanking(@PathVariable("rankingId") Long rankingId, @PathVariable("userId") Long userId) throws AccessDeniedException {
    numberedRankingService.deleteNumberedRankingByIdAndUser(rankingId, userId);
  }
}
