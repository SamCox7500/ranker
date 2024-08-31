package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRankingService;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
public class MediaListController {

  private final MediaListService mediaListService;
  private final NumberedRankingService numberedRankingService;

  public MediaListController(MediaListService mediaListService, NumberedRankingService numberedRankingService) {
    this.mediaListService = mediaListService;
    this.numberedRankingService = numberedRankingService;
  }
  @GetMapping("/users/{userId}/numberedrankings/{rankingId}/medialist")
  public MediaListDTO getMediaList(@PathVariable Long userId, @PathVariable Long rankingId) throws AccessDeniedException {
    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
    return MediaListDTOMapper.toMediaListDTO(mediaList);
  }
  @PostMapping("/users/{userId}/numberedrankings/{rankingId}/mediaList/entries")
  public void addEntryToMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @PathVariable Long mediaListId,
    @RequestBody MediaListEntryDTO entryDTO
  ) throws AccessDeniedException {
    mediaListService.addEntryToList(mediaListId, entryDTO);
  }
}
