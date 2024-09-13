package com.samcox.ranker.media;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.ranking.*;
import com.samcox.ranker.tmdb.TmdbService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@Service
@Validated
public class MediaListService {

  private final MediaListRepository mediaListRepository;
  private final NumberedRankingService numberedRankingService;
  private final MediaListEntryService mediaListEntryService;
  private final AuthService authService;

  private final TmdbService tmdbService;

  public MediaListService(
    MediaListRepository mediaListRepository,
    NumberedRankingService numberedRankingService,
    MediaListEntryService mediaListEntryService,
    AuthService authService,
    TmdbService tmdbService
  ) {
    this.mediaListRepository = mediaListRepository;
    this.numberedRankingService = numberedRankingService;
    this.mediaListEntryService = mediaListEntryService;
    this.authService = authService;
    this.tmdbService = tmdbService;
  }

  public MediaList getMediaListByNumberedRankingAndUser(Long numberedRankingId, Long userId) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByUserAndId(numberedRankingId, userId);
    return mediaListRepository.findByNumberedRanking(numberedRanking)
      .orElseThrow(() -> new MediaListNotFoundException("Media list cannot be found for numbered ranking: "
        + numberedRankingId));
  }
  /*
  public MediaList getMediaListById(Long id) {
    return mediaListRepository.findById(id)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id " + id));
  }
   */
  public void moveEntryInList(Long mediaListId, int oldPosition, int newPosition) throws AccessDeniedException {
    checkOwnership(mediaListId);

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id: " + mediaListId));

    if (oldPosition < 1 || oldPosition > mediaList.getEntries().size()) {
      throw new RankingOutOfBoundsException("Old position out of bounds");
    }
    if (newPosition < 1 || newPosition > mediaList.getEntries().size()) {
      throw new RankingOutOfBoundsException("New position out of bounds");
    }

    mediaList.moveEntry(oldPosition, newPosition);
    mediaListRepository.save(mediaList);
  }
  public void removeEntryInList(Long mediaListId, Long entryId) throws AccessDeniedException {
    checkOwnership(mediaListId);

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("Media list not found with id: " + mediaListId));

    MediaListEntry entry = mediaListEntryService.getMediaListEntryById(entryId);
    mediaList.removeEntry(entry);
    mediaListRepository.save(mediaList);
  }
  public void removeEntriesInList(Long mediaListId, List<Long> mediaListEntryIds) throws AccessDeniedException {
    checkOwnership(mediaListId);
    if (mediaListEntryIds.isEmpty()) {
      throw new MediaListEntryNotFoundException("No media lists could be found to be deleted because no IDs were provided");
    }

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("Media list not found with id: " + mediaListId));

    List<MediaListEntry> mediaListEntriesRemoveList = new ArrayList<>();

    for (Long mediaListEntryId: mediaListEntryIds) {
      MediaListEntry mediaListEntry = mediaListEntryService.getMediaListEntryByMediaListAndId(mediaList, mediaListEntryId);
      mediaListEntriesRemoveList.add(mediaListEntry);
    }
    mediaList.removeEntries(mediaListEntriesRemoveList);
  }
  public void addEntryToList(@Valid EntryAddRequest entryAddRequest, Long mediaListId) throws AccessDeniedException {
    checkOwnership(mediaListId);

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id: " + mediaListId));

    if (entryAddRequest.getRanking() < 1 || entryAddRequest.getRanking() > mediaList.getEntries().size() + 1) {
      throw new RankingOutOfBoundsException("Ranking for entry is out of bounds");
    }

    if (mediaList.hasEntryWithTmdbId(entryAddRequest.getTmdbId())) {
      throw new DuplicateMediaEntryException("tmdb id " + entryAddRequest.getTmdbId() + " already exists in list");
    }

    MediaListEntry mediaListEntry = new MediaListEntry();
    mediaListEntry.setRanking(entryAddRequest.getRanking());
    mediaListEntry.setMediaList(mediaList);
    mediaListEntry.setTmdbId(entryAddRequest.getTmdbId());

    mediaList.addEntry(mediaListEntry);
    mediaListRepository.save(mediaList);
  }
  public void checkOwnership(Long mediaListId) throws AccessDeniedException {
    Long authUserId = authService.getAuthenticatedUser().getId();

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("Could not check ownership as mediaList does not exist with id: " + mediaListId));
    if (!mediaList.getNumberedRanking().getUser().getId().equals(authUserId)) {
      throw new AccessDeniedException("You do not have permission to access that resource");
    }
  }
  public MediaListDTO toMediaListDTO(MediaList mediaList) {

    //Creating list of DTOs depending on the media type
    List<MediaListEntryDTO> mediaListEntryDTOS = null;

    mediaListEntryDTOS = toMediaDTO(mediaList.getEntries(), mediaList.getMediaType());

    NumberedRankingDTO numberedRankingDTO = NumberedRankingDTOMapper.toNumberedRankingDTO(mediaList.getNumberedRanking());

    return new MediaListDTO(mediaList.getId(), mediaList.getMediaType(), mediaListEntryDTOS, numberedRankingDTO);
  }
  private List<MediaListEntryDTO> toMediaDTO(List<MediaListEntry> mediaListEntries, MediaType mediaType) {
    List<MediaListEntryDTO> mediaDTOList = new ArrayList<>();

    for (MediaListEntry entry: mediaListEntries) {
      if (mediaType.equals(MediaType.FILM)) {
        FilmDTO filmDTO = tmdbService.getFilmDetails(entry.getTmdbId());
        filmDTO.setId(entry.getId());
        filmDTO.setRanking(entry.getRanking());
        mediaDTOList.add(filmDTO);
      } else if (mediaType.equals(MediaType.TV_SHOW)) {
        TVShowDTO tvShowDTO = tmdbService.getTVShowDetails(entry.getTmdbId());
        tvShowDTO.setId(entry.getId());
        tvShowDTO.setRanking(entry.getRanking());
        mediaDTOList.add(tvShowDTO);
      }
    }
    return mediaDTOList;
  }
}
