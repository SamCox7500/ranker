package com.samcox.ranker.ranking;

import com.samcox.ranker.user.UserDTOMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting {@link Ranking} to {@link RankingDTO}.
 * @see Ranking
 * @see RankingDTO
 */
public class RankingDTOMapper {
  /**
   * Static method for converting a single {@link Ranking} to a {@link RankingDTO}.
   * @param ranking the ranking to be converted to a DTO.
   * @return the ranking as a {@code RankingDTO}
   */
  public static RankingDTO toRankingDTO(Ranking ranking) {
    return new RankingDTO(
      ranking.getId(),
      ranking.getTitle(),
      ranking.getDescription(),
      ranking.getIsPublic(),
      ranking.getRankingType().toString(),
      ranking.getMediaType().toString());
  }
  /**
   * Static method for converting a list of {@link Ranking} to a list of {@link RankingDTO}.
   * @param rankings the list of rankings to be converted
   * @return the list of {@code RankingDTO}
   */
  public static List<RankingDTO> toRankingDTOs(List<Ranking> rankings) {
    return rankings.stream()
      .map(RankingDTOMapper::toRankingDTO)
      .collect(Collectors.toList());
  }
}
