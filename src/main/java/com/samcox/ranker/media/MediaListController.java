package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRankingService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
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

  //@GetMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}")
  @GetMapping("/users/{userId}/numberedrankings/{rankingId}/medialist")
  public MediaListDTO getMediaList(@PathVariable Long userId, @PathVariable Long rankingId) throws AccessDeniedException {

    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
    return mediaListService.toMediaListDTO(mediaList);

  }
  //@PostMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries")
  @PostMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries")
  public void addEntryToMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @RequestBody @Valid EntryAddRequest entryAddRequest
  ) throws AccessDeniedException {
    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
    mediaListService.addEntryToList(entryAddRequest, mediaList.getId());
  }
  //@PutMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries/{entryId}")
  @PutMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries/{entryId}")
  @Transactional
  public void moveEntryInMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @PathVariable Long entryId,
    @RequestBody @Valid EntryMoveRequest entryMoveRequest
  ) throws AccessDeniedException {
      MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
      MediaListEntry mediaListEntry = mediaListEntryService.getMediaListEntryById(entryMoveRequest.getEntryId());
    //System.out.println("MediaListEntryId: " + mediaListEntry.getId() + " Ranking: " + mediaListEntry.getRanking() + " New ranking: " + entryMoveRequest.getNewPosition());
      int oldPosition = mediaListEntry.getRanking();
      int newPosition = entryMoveRequest.getNewPosition();
    //System.out.println("Before moving: " + mediaList.getEntries());
    //System.out.println("Moving entry with id " + entryMoveRequest.getEntryId() + " from position " + oldPosition + " to " + newPosition);
      mediaListService.moveEntryInList(mediaList.getId(), oldPosition, newPosition);
    //System.out.println("After moving: " + mediaList.getEntries());
      //System.out.println("OldPosition: " + oldPosition + " New Position: " + newPosition);
      //System.out.println(mediaList.getEntries());


      //todo change to be mediaListId, mediaListEntryId, newPosition
  }
  //@DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries/{entryId}")
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries/{entryId}")
  public void removeEntryFromMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @PathVariable Long entryId
  ) throws AccessDeniedException {
      MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
      mediaListService.removeEntryInList(mediaList.getId(), entryId);
  }
  //@DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/{mediaListId}/entries")
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}/medialist/entries")
  public void removeEntriesFromMediaList(
    @PathVariable Long userId,
    @PathVariable Long rankingId,
    //@PathVariable Long mediaListId,
    @RequestBody List<Long> entryIds
    ) throws AccessDeniedException {
    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(rankingId, userId);
    mediaListService.removeEntriesInList(mediaList.getId(), entryIds);
  }
}
