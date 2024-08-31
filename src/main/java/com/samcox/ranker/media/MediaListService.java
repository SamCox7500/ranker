package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRanking;
import com.samcox.ranker.ranking.NumberedRankingDTO;
import com.samcox.ranker.ranking.NumberedRankingService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
@Valid
public class MediaListService {

  private final MediaListRepository mediaListRepository;
  private final NumberedRankingService numberedRankingService;

  private final MediaListEntryService mediaListEntryService;

  public MediaListService(MediaListRepository mediaListRepository, NumberedRankingService numberedRankingService, MediaListEntryService mediaListEntryService) {
    this.mediaListRepository = mediaListRepository;
    this.numberedRankingService = numberedRankingService;
    this.mediaListEntryService = mediaListEntryService;
  }

  public MediaList getMediaListByNumberedRanking(@Valid NumberedRankingDTO numberedRankingDTO) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByUserAndId(
      numberedRankingDTO.getId(),
      numberedRankingDTO.getUserDTO().getId()
    );
    return mediaListRepository.findByNumberedRanking(numberedRanking)
      .orElseThrow(() -> new MediaListNotFoundException("Media list cannot be found for numbered ranking: " +
        numberedRankingDTO.getId()));
  }
  public MediaList getMediaListById(Long id) {
    return mediaListRepository.findById(id)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id " + id));
  }
  public void createMediaList(@Valid MediaListDTO mediaListDTO) throws AccessDeniedException {
    Long userId = mediaListDTO.getNumberedRankingDTO().getUserDTO().getId();
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByUserAndId(mediaListDTO.getNumberedRankingDTO().getId(), userId);

    List<MediaListEntry> mediaListEntries = new ArrayList<>();

    MediaList mediaList = new MediaList();
    mediaList.setNumberedRanking(numberedRanking);
    mediaList.setMediaType(mediaListDTO.getMediaType());
    mediaList.setEntries(mediaListEntries);

    mediaListRepository.save(mediaList);
  }
  public void moveEntryInList(Long mediaListId, int oldPosition, int newPosition) {
    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id: " + mediaListId));
    mediaList.moveEntry(oldPosition, newPosition);
    mediaListRepository.save(mediaList);
  }
  public void removeEntryInList(Long mediaListId, Long entryId) {
    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("Media list not found with id: " + mediaListId));

    MediaListEntry entry = mediaListEntryService.getMediaListEntryById(entryId);
    mediaList.removeEntry(entry);
    mediaListRepository.save(mediaList);
  }
  public void addEntryToList(Long mediaListId, MediaListEntry newEntry) {
    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id: " + mediaListId));
    mediaList.addEntry(newEntry);
    mediaListRepository.save(mediaList);
  }
  public void deleteMediaListByNumberedRankingAndUser(Long numberedRankingId, Long userId) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByUserAndId(numberedRankingId, userId);
    MediaList mediaList = mediaListRepository.findByNumberedRanking(numberedRanking)
      .orElseThrow(() -> new MediaListNotFoundException("Media list not found for numbered ranking: " +
        numberedRankingId + " and user: " + userId));

    mediaListRepository.delete(mediaList);
  }
  //todo use auth service
  //todo use MediaListEntryService
}
