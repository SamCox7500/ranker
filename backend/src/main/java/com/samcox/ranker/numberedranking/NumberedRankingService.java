package com.samcox.ranker.numberedranking;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.media.InvalidMediaTypeException;
import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.media.MediaType;
import com.samcox.ranker.ranking.RankingNotFoundException;
import com.samcox.ranker.ranking.UpdateRankingDTO;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserNotFoundException;
import com.samcox.ranker.user.UserService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * Service class for handling business logic related to {@link NumberedRanking} operations.
 *
 * <p>This class provides functionality for numbered rankings including: creating them,
 * retrieving them, updating them, deleting them, checking ownership.
 * It enforces authorization checks to ensure that users can only access or modify their own data</p>
 *
 * <p>Marked as {@code @Validated} to support validation on method arguments.</p>
 *
 * @see NumberedRanking
 *
 */
@Validated
@Service
public class NumberedRankingService {

  /**
   * The repository for accessing numbered ranking data
   */
  private final NumberedRankingRepository numberedRankingRepository;

  /**
   * The service used to perform user operations.
   * <p>Mainly used for retrieving user data on the user who owns a numbered ranking.</p>
   */
  private final UserService userService;

  /**
   * The service used to perform authentication and authorisation checks.
   */
  private final AuthService authService;

  /**
   * Creates a new numbered ranking service instance.
   * @param numberedRankingRepository the repository for performing numbered ranking operations
   * @param userService the service for performing user operations
   * @param authService the service for checking authentication and authorisation
   */
  public NumberedRankingService(NumberedRankingRepository numberedRankingRepository, UserService userService, AuthService authService) {
    this.numberedRankingRepository = numberedRankingRepository;
    this.userService = userService;
    this.authService = authService;
  }

  /**
   * Fetches a numbered ranking by id and userID.
   * @param numberedRankingId the id of the numbered ranking to be fetched
   * @param userId the id of the user that is fetching the ranking
   * @return the numbered ranking belonging to that user and id.
   * @throws AccessDeniedException if the currently authenticated user does not have permission to access this numbered ranking.
   */
  public NumberedRanking getNumberedRankingByIdAndUser(Long numberedRankingId, Long userId) throws AccessDeniedException {
    checkPermissions(numberedRankingId);
    User user = userService.getUserByID(userId);
    return numberedRankingRepository.findByIdAndUser(numberedRankingId, user)
      .orElseThrow(
        () -> new RankingNotFoundException("Numbered ranking was not found with id: " + numberedRankingId
          + " and user: " + userId)
      );
  }

  /**
   * Returns a list of numbered ranking belonging to a user
   * @param userId the id of the user who owns the numbered rankings
   * @return the list of numbered rankings belonging to the user
   * @throws AccessDeniedException if the currently authenticated user does not have permission to access this user's rankings.
   */
  public List<NumberedRanking> getAllNumberedRankingsByUser(Long userId) throws AccessDeniedException {
    User user = userService.getUserByID(userId);
    return numberedRankingRepository.findByUser(user)
      .orElseThrow(() -> new UserNotFoundException("Cannot retrieve numbered rankings because user does not exist"));
  }

  /**
   * Creates a new numbered ranking from a numbered ranking DTO.
   * @param createNumberedRankingDTO the dto that holds the data used to create the new numbered ranking
   * @throws AccessDeniedException if the current authenticated user does not have permission to create a numbered ranking with the data in this DTO.
   * E.g. creating a ranking for a different user.
   */
  public void createNumberedRanking(Long userId, @Valid CreateNumberedRankingDTO createNumberedRankingDTO) throws AccessDeniedException {

    checkPermissions(userId);
    /*
    The media type must be a known media type e.g., "FILM", "TV_SHOW"
     */
    try {
      MediaType mediaType = MediaType.valueOf(createNumberedRankingDTO.getMediaType());
    } catch (IllegalArgumentException e) {
      throw new InvalidMediaTypeException("Invalid media type: " + createNumberedRankingDTO.getMediaType());
    }

    User user = userService.getUserByID(userId);

    MediaList mediaList = new MediaList();
    mediaList.setMediaType(MediaType.valueOf(createNumberedRankingDTO.getMediaType()));

    NumberedRanking numberedRanking = new NumberedRanking();
    numberedRanking.setUser(user);
    numberedRanking.setTitle(createNumberedRankingDTO.getTitle());
    numberedRanking.setDescription(createNumberedRankingDTO.getDescription());
    numberedRanking.setPrivate(); //todo not yet implemented
    numberedRanking.setReverseOrder(createNumberedRankingDTO.isReverseOrder());
    numberedRanking.setMediaType(mediaList.getMediaType());

    numberedRanking.setMediaList(mediaList);

    numberedRankingRepository.save(numberedRanking);
  }

  /**
   * Updates the numbered ranking with the data from the NumberedRankingDTO
   * @param updateNumberedRankingDTO the new data to be used to update the numbered ranking
   * @throws AccessDeniedException if the currently authenticated user does not have permission to access this user
   */
  public void updateNumberedRanking(Long rankingId, Long userId, @Valid UpdateNumberedRankingDTO updateNumberedRankingDTO) throws AccessDeniedException {
    checkPermissions(userId);
    if (rankingId == null) {
      throw new RankingNotFoundException("Ranking could not be found because ID is null");
    }

    NumberedRanking oldNumberedRanking = numberedRankingRepository.findById(rankingId)
      .orElseThrow(() -> new RankingNotFoundException("Numbered ranking does not exist with id: " + rankingId));
    oldNumberedRanking.setTitle(updateNumberedRankingDTO.getTitle());
    oldNumberedRanking.setDescription(updateNumberedRankingDTO.getDescription());
    oldNumberedRanking.setPrivate(); //todo
    oldNumberedRanking.setReverseOrder(updateNumberedRankingDTO.isReverseOrder());
    numberedRankingRepository.save(oldNumberedRanking);
  }

  /**
   * Deletes a numbered ranking belonging to a user by id.
   * @param rankingId the id of the numbered ranking to be deleted
   * @param userId the id of the user trying to delete the numbered ranking
   * @throws AccessDeniedException if the user trying to delete the ranking does not have permission
   */
  public void deleteNumberedRankingByIdAndUser(Long rankingId, Long userId) throws AccessDeniedException {
    checkPermissions(userId);
    if (numberedRankingRepository.findById(rankingId).isEmpty()) {
      throw new RankingNotFoundException("Cannot delete ranking. Ranking does not exist");
    }
    User user = userService.getUserByID(userId);
    numberedRankingRepository.deleteByIdAndUser(rankingId, user);
  }

  /**
   * Checks if the currently authenticated user has access to a numbered ranking
   * @param userId the id of the user to be checked for authorisation
   * @throws AccessDeniedException if the current authenticated user does not have permission to access this ranking
   */
  public void checkPermissions(Long userId) throws AccessDeniedException {
    UserDTO authUser = authService.getAuthenticatedUser();
    if (!authUser.getId().equals(userId)) {
      throw new AccessDeniedException("You do not have permission to view this ranking");
    }
  }
}
