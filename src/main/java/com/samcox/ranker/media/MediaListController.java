package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRankingService;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
public class MediaListController {

  private final MediaListService mediaListService;
  private final MediaListEntryService mediaListEntryService;
  private final NumberedRankingService numberedRankingService;

  public MediaListController(MediaListService mediaListService, MediaListEntryService mediaListEntryService, NumberedRankingService numberedRankingService) {
    this.mediaListService = mediaListService;
    this.mediaListEntryService = mediaListEntryService;
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
  @PutMapping("/users/{usersId}/numberedrankings/{rankingId}/mediaList/entries/{entryId}")
  public void moveEntryInMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @PathVariable Long mediaListId,
    @PathVariable Long entryId,
    @RequestBody MediaListEntryDTO mediaListEntryDTO
  ) throws AccessDeniedException {
      MediaListEntry mediaListEntry = mediaListEntryService.getMediaListEntryById(mediaListEntryDTO.getId());
      int oldPosition = mediaListEntry.getRanking();
      int newPosition = mediaListEntryDTO.getRanking();
      mediaListService.moveEntryInList(mediaListId, oldPosition, newPosition);
  }
  @DeleteMapping("/users/{usersId}/numberedrankings/{rankingId}/mediaList/entries/{entryId}")
  public void removeEntryFromMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @PathVariable Long mediaListId,
    @PathVariable Long entryId
  ) throws AccessDeniedException {
      mediaListService.removeEntryInList(mediaListId, entryId);
  }
}
