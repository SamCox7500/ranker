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
      UserDTOMapper.toUserDTO(numberedRanking.getUser()),
      numberedRanking.getTitle(),
      numberedRanking.getDescription(),
      numberedRanking.getIsPublic(),
      numberedRanking.getIsReverseOrder(),
      numberedRanking.getMediaType().toString());
  }

  /**
   * Static method for converting a list of {@link NumberedRanking} to a list of {@link NumberedRankingDTO}.
   * @param rankings the list of numbered rankings to be converted
   * @return the list of {@code NumberedRankingDTO}
   */
  public static List<NumberedRankingDTO> toNumberedRankingDTOs(List<NumberedRanking> rankings) {
    return rankings.stream()
      .map(NumberedRankingDTOMapper::toNumberedRankingDTO)
      .collect(Collectors.toList());
  }
}
