package com.samcox.ranker.numberedranking;
import com.samcox.ranker.user.UserDTOMapper;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper class for converting {@link NumberedRanking} to {@link NumberedRankingDTO}.
 * @see NumberedRanking
 * @see NumberedRankingDTO
 */
public class NumberedRankingDTOMapper {
  /**
   * Static method for converting a single {@link NumberedRanking} to a {@link NumberedRankingDTO}.
   * @param numberedRanking the numbered ranking to be converted to a DTO.
   * @return the numbered ranking as a {@code NumberedRankingDTO}
   */
  public static NumberedRankingDTO toNumberedRankingDTO(NumberedRanking numberedRanking) {
    return new NumberedRankingDTO(
      numberedRanking.getId(),
      numberedRanking.getTitle(),
      numberedRanking.getDescription(),
      numberedRanking.getIsPublic(),
      numberedRanking.getMediaType().toString(),
      numberedRanking.getRankingType().toString(),
      numberedRanking.getIsReverseOrder());
  }
}
