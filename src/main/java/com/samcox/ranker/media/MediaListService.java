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
  public void removeEntriesInList(Long mediaListId, List<Long> mediaListEntryIds) throws AccessDeniedException {
    checkOwnership(mediaListId);

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

    MediaListEntry mediaListEntry = new MediaListEntry();
    mediaListEntry.setRanking(entryAddRequest.getRanking());
    mediaListEntry.setMediaList(mediaList);
    mediaListEntry.setTmdbId(entryAddRequest.getTmdbId());

    mediaList.addEntry(mediaListEntry);
    mediaListRepository.save(mediaList);
  }
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

    //Creating list of DTOs depending on the media type
    List<MediaListEntryDTO> mediaListEntries = null;
    if (mediaList.getMediaType().equals(MediaType.FILM)) {
      mediaListEntries = toFilmListDTOs(mediaList.getEntries());
    } else if (mediaList.getMediaType().equals(MediaType.TV_SHOW)) {
      //todo tv show
    } else {
      throw new IllegalArgumentException("Invalid media type for media list: " + mediaList.getId());
    }
    NumberedRankingDTO numberedRankingDTO = NumberedRankingDTOMapper.toNumberedRankingDTO(mediaList.getNumberedRanking());

    return new MediaListDTO(mediaList.getId(), mediaList.getMediaType(), mediaListEntries, numberedRankingDTO);
  }
  private List<MediaListEntryDTO> toFilmListDTOs(List<MediaListEntry> mediaListEntries) {
    List<MediaListEntryDTO> filmDTOList = new ArrayList<>();

    for (MediaListEntry entry: mediaListEntries) {
      FilmDTO filmDTO = tmdbService.getFilmDetails(entry.getTmdbId());
      filmDTO.setId(entry.getTmdbId());
      filmDTO.setRanking(entry.getRanking());
      filmDTOList.add(filmDTO);
    }
    return filmDTOList;
  }
}
