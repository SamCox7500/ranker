package com.samcox.ranker.ranking;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
public class RankingController {

  private final RankingService rankingService;


  public RankingController(RankingService rankingService) {
    this.rankingService = rankingService;
  }

  @GetMapping("/users/{userId}/rankings")
  public List<RankingDTO> getAllRankings(@PathVariable("userId") Long userId) throws AccessDeniedException {
    return RankingDTOMapper.toRankingDTOs(rankingService.getAllRankingsByUser(userId));
  }
  @GetMapping("/users/{userId}/rankings/{rankingId}")
  public RankingDTO getRanking(@PathVariable("userId") Long userId, @PathVariable("rankingId") Long rankerId) throws AccessDeniedException {
    return RankingDTOMapper.toRankingDTO(rankingService.getRankingByIdAndUser(rankerId, userId));
  }

}
