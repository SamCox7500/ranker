package com.samcox.ranker.media;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.ranking.*;
import com.samcox.ranker.tmdb.TmdbService;
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
  public void moveEntryInList(Long mediaListId, int oldPosition, int newPosition) throws AccessDeniedException {
    checkOwnership(mediaListId);

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id: " + mediaListId));
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
  //todo method for removing multiple entries at once
  public void addEntryToList(@Valid EntryAddRequest entryAddRequest, Long mediaListId) throws AccessDeniedException {
    checkOwnership(mediaListId);

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id: " + mediaListId));

    MediaListEntry mediaListEntry = new MediaListEntry();
    mediaListEntry.setRanking(entryAddRequest.getRanking());
    mediaListEntry.setMediaList(mediaList);
    mediaListEntry.setTmdbId(entryAddRequest.getTmdbId());

    mediaList.addEntry(mediaListEntry);
    mediaListRepository.save(mediaList);
  }
  /*
  public void deleteMediaListByNumberedRankingAndUser(Long numberedRankingId, Long userId) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByUserAndId(numberedRankingId, userId);
    MediaList mediaList = mediaListRepository.findByNumberedRanking(numberedRanking)
      .orElseThrow(() -> new MediaListNotFoundException("Media list not found for numbered ranking: " +
        numberedRankingId + " and user: " + userId));

    mediaListRepository.delete(mediaList);
  }
   */
  //todo use auth service
  public void checkOwnership(Long mediaListId) throws AccessDeniedException {
    Long authUserId = authService.getAuthenticatedUser().getId();

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("Could not check ownership as mediaList does not exist with id: " + mediaListId));
    if (!mediaList.getNumberedRanking().getUser().getId().equals(authUserId)) {
      throw new AccessDeniedException("You do not have permission to access that resource");
    }
  }
  public MediaListDTO toMediaListDTO(MediaList mediaList) {
    List<MediaListEntryDTO> entryDTOS = toMediaListEntryDTOS(mediaList.getMediaType(), mediaList.getEntries());

    NumberedRankingDTO numberedRankingDTO = NumberedRankingDTOMapper.toNumberedRankingDTO(mediaList.getNumberedRanking());

    return new MediaListDTO(mediaList.getId(), mediaList.getMediaType(), entryDTOS, numberedRankingDTO);
  }
  private List<MediaListEntryDTO> toMediaListEntryDTOS (MediaType mediaType, List<MediaListEntry> mediaListEntries) {
    List<MediaListEntryDTO> entries = new ArrayList<>();

    for (MediaListEntry entry: mediaListEntries) {

      MediaListEntryDTO mediaListEntryDTO = new MediaListEntryDTO();
      mediaListEntryDTO.setRanking(entry.getRanking());

      if (mediaType == MediaType.FILM) {
        FilmDTO filmDTO = tmdbService.getFilmDetails(entry.getTmdbId());
        mediaListEntryDTO.setMediaDTO(filmDTO);
      } else if (mediaType == MediaType.TV_SHOW) {
        //todo tv
      } else {
        //todo throw exception
      }
      entries.add(mediaListEntryDTO);
    }
    return entries;
  }
}
