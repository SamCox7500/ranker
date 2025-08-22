package com.samcox.ranker.media;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingService;
import com.samcox.ranker.ranking.MediaType;
import com.samcox.ranker.tmdb.TmdbService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

/**
 * Service class for handling business logic related to {@link MediaList} operations.
 *
 * <p>This class provides functionality for retrieving media lists, removing entries from a media list,
 * adding new entries to a media list and moving entries to different rankings.</p>
 *
 * <p>Marked as {@code Validated} to support validation on method arguments.</p>
 *
 * @see MediaList
 * @see MediaListEntry
 * @see MediaListEntryDTO
 */
@Service
@Validated
public class MediaListService {

  /**
   * The repository for accessing media list data.
   */
  private final MediaListRepository mediaListRepository;
  /**
   * The service used to perform numbered ranking operations.
   * <p>Mainly used for retrieving the title, description and media type of the media list.</p>
   */
  private final NumberedRankingService numberedRankingService;
  /**
   * The service used to perform operations on media list entries.
   */
  private final MediaListEntryService mediaListEntryService;
  /**
   * The service used to perform authentication and authorisation checks.
   */
  private final AuthService authService;

  /**
   * The service used to perform operations relating to media in TMDB API.
   */
  private final TmdbService tmdbService;

  /**
   * The constructor for media list service.
   * @param mediaListRepository repository for accessing media list data
   * @param numberedRankingService the service for performing numbered ranking related operations
   * @param mediaListEntryService the service for performing media list entry related operations
   * @param authService the auth service for performing authentication and authorisation checks
   * @param tmdbService the service for performing operations relating to TMDB API
   */
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

  /**
   * Returns a media list for a specified numbered ranking and user.
   * @param numberedRankingId the id of the numbered ranking that is attached to the media list
   * @param userId the id of the user that owns the media list
   * @return the media list for the specified user and numbered ranking
   * @throws AccessDeniedException if the user trying to access the media list does not have permission
   */
  public MediaList getMediaListByNumberedRankingAndUser(Long numberedRankingId, Long userId) throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByIdAndUser(numberedRankingId, userId);
    return numberedRanking.getMediaList();
    /*
    return mediaListRepository.findByNumberedRanking(numberedRanking)
      .orElseThrow(() -> new MediaListNotFoundException("Media list cannot be found for numbered ranking: "
        + numberedRankingId));
     */
  }

  /**
   * Moves a {@link MediaListEntry} to a different ranking.
   * @param mediaListId the of the media list containing the entry to be moved
   * @param oldPosition the old ranking of the entry
   * @param newPosition the ranking to which the entry is to be moved
   * @throws AccessDeniedException if the user trying to move the entry does not have permission
   */
  @Transactional
  public void moveEntryInList(Long userId, Long mediaListId, int oldPosition, int newPosition) throws AccessDeniedException {
    checkOwnership(userId, mediaListId);

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

  /**
   * Removes a {@link MediaListEntry} from a media list.
   * @param mediaListId the id of the media list containing the media entry to be removed
   * @param entryId the id of the entry to be removed
   * @throws AccessDeniedException if the user trying to remove the entry does not have permission
   */
  public void removeEntryInList(Long userId, Long mediaListId, Long entryId) throws AccessDeniedException {
    checkOwnership(userId, mediaListId);

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() -> new MediaListNotFoundException("Media list not found with id: " + mediaListId));

    MediaListEntry entry = mediaListEntryService.getMediaListEntryById(userId,entryId);
    mediaList.removeEntry(entry);
    mediaListRepository.save(mediaList);
  }

  /**
   * Removes multiple {@link MediaListEntry} from a media list.
   * @param mediaListId the id of the media list containing the entries to be removed
   * @param mediaListEntryIds the list of ids of the entries to be removed
   * @throws AccessDeniedException if the user trying to remove the entries does not have permission
   */
  public void removeEntriesInList(Long userId, Long mediaListId, List<Long> mediaListEntryIds) throws AccessDeniedException {
    checkOwnership(userId, mediaListId);
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

  /**
   * Adds a new entry to the media list
   * @param entryAddRequest the DTO for the new entry to be added
   * @param mediaListId the id of the media list to which the entry is to be added
   * @throws AccessDeniedException if the user trying to add the entry does not have permission to do so
   */
  public void addEntryToList(Long userId, @Valid EntryAddRequest entryAddRequest, Long mediaListId) throws AccessDeniedException {
    checkOwnership(userId, mediaListId);

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

  /**
   * Ensures that the user trying to access a media list has permission to do so. Otherwise, an exception is thrown.
   * @param mediaListId the id of the media list the user is trying to access
   * @throws AccessDeniedException if the user does not have permission to access the media list
   */
  public void checkOwnership(Long userId, Long mediaListId) throws AccessDeniedException {
    Long authUserId = authService.getAuthenticatedUser().getId();

    MediaList mediaList = mediaListRepository.findById(mediaListId)
      .orElseThrow(() ->
        new MediaListNotFoundException("Could not check ownership as mediaList does not exist with id: " + mediaListId));
    if (!userId.equals(authUserId)) {
      throw new AccessDeniedException("You do not have permission to access that resource");
    }
  }

  /**
   * Returns a {@link MediaList} as a {@link MediaListEntryDTO}
   * @param mediaList the media list to be represented as a dto
   * @return the DTO representation of the media list
   */
  public MediaListDTO toMediaListDTO(MediaList mediaList) {

    //Creating list of DTOs depending on the media type
    List<MediaListEntryDTO> mediaListEntryDTOS = null;

    mediaListEntryDTOS = toMediaDTO(mediaList.getEntries(), mediaList.getMediaType());


    return new MediaListDTO(mediaList.getId(), mediaList.getMediaType(), mediaListEntryDTOS);
  }

  /**
   * Returns a list of {@link MediaListEntry} as a list of {@link MediaListEntryDTO}
   * @param mediaListEntries the list of entries to be represented as a list of DTOs
   * @param mediaType the media type of the entries
   * @return a list of DTO representations of media list entries
   */
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
    /*
  public MediaList getMediaListById(Long id) {
    return mediaListRepository.findById(id)
      .orElseThrow(() -> new MediaListNotFoundException("MediaList not found with id " + id));
  }
   */
}
