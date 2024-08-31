package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRanking;
import com.samcox.ranker.ranking.NumberedRankingDTO;
import com.samcox.ranker.ranking.NumberedRankingDTOMapper;
import com.samcox.ranker.user.UserDTOMapper;

import java.util.List;
import java.util.stream.Collectors;

public class MediaListDTOMapper {
  public static MediaListDTO toMediaListDTO(MediaList mediaList) {
      return new MediaListDTO(
        mediaList.getId(),
        mediaList.getMediaType(),
        MediaListEntryDTOMapper.toMediaListEntryDTOs(mediaList.getEntries()),
        NumberedRankingDTOMapper.toNumberedRankingDTO(mediaList.getNumberedRanking()));
    }
}
