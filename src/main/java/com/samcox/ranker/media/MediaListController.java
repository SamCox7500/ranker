package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRankingService;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

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
    return mediaListService.toMediaListDTO(mediaList);

  }
  //todo think about this works in the frontend
  @PostMapping("/users/{userId}/numberedrankings/{rankingId}/mediaList/entries")
  public void addEntryToMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @PathVariable Long mediaListId,
    @RequestBody EntryAddRequest entryAddRequest
  ) throws AccessDeniedException {
    mediaListService.addEntryToList(entryAddRequest, mediaListId);
  }
  @PutMapping("/users/{usersId}/numberedrankings/{rankingId}/mediaList/entries/{entryId}")
  public void moveEntryInMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @PathVariable Long mediaListId,
    @PathVariable Long entryId,
    @RequestBody EntryMoveRequest entryMoveRequest
  ) throws AccessDeniedException {
      MediaListEntry mediaListEntry = mediaListEntryService.getMediaListEntryById(entryMoveRequest.getEntryId());
      int oldPosition = mediaListEntry.getRanking();
      int newPosition = entryMoveRequest.getNewPosition();
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
  //todo removing multiple entries
  @DeleteMapping("/users/{usersId}/numberedrankings/{rankingId}/mediaList/entries")
  public void removeEntriesFromMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    @PathVariable Long mediaListId,
    @RequestBody List<Long> entryIds
    ) throws AccessDeniedException {
    mediaListService.removeEntriesInList(mediaListId, entryIds);
  }
}
