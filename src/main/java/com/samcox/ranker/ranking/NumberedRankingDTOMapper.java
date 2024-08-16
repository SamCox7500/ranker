package com.samcox.ranker.ranking;
import com.samcox.ranker.user.UserDTOMapper;

import java.util.List;
import java.util.stream.Collectors;

public class NumberedRankingDTOMapper {
  public static NumberedRankingDTO toNumberedRankingDTO(NumberedRanking numberedRanking) {
    return new NumberedRankingDTO(
      numberedRanking.getId(),
      UserDTOMapper.toUserDTO(numberedRanking.getUser()),
      numberedRanking.getTitle(),
      numberedRanking.getDescription(),
      numberedRanking.getIsPublic(),
      numberedRanking.getIsReverseOrder());
  }
  public static List<NumberedRankingDTO> toNumberedRankingDTOs(List<NumberedRanking> rankings) {
    return rankings.stream()
      .map(NumberedRankingDTOMapper::toNumberedRankingDTO)
      .collect(Collectors.toList());
  }
}
