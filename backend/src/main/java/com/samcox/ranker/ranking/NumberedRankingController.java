package com.samcox.ranker.ranking;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;
import java.util.List;

/**
 * REST controller for handling numbered ranking operations.
 *
 * <p>This controller provides endpoints to perform CRUD operations on {@link NumberedRanking}s
 *  including creating a numbered ranking, retrieving one, updating a numbered ranking,
 *  and deleting a numbered ranking.</p>
 *
 * @see NumberedRanking
 * @see NumberedRankingService
 * @see NumberedRankingDTO
 */
@RestController
public class NumberedRankingController {

  /**
   * The service layer the controller uses to perform numbered ranking operations. See {@link NumberedRankingService}.
   */
  private final NumberedRankingService numberedRankingService;

  /**
   * Creates a new numbered ranking controller instance.
   * @param numberedRankingService the service to be used by the controller for performing operations on numbered rankings.
   */
  public NumberedRankingController(NumberedRankingService numberedRankingService) {
    this.numberedRankingService = numberedRankingService;
  }

  /**
   * Returns a list of all numbered rankings belonging to a user.
   * @param userId the id of the user used to fetch all numbered rankings for
   * @return the list of all numbered rankings as DTOs
   * @throws AccessDeniedException if the authenticated user does not have authorisation to access rankings for that user.
   */
  @GetMapping("/users/{userId}/numberedrankings")
  public List<NumberedRankingDTO> getAllRankings(@PathVariable("userId") Long userId) throws AccessDeniedException {
    return NumberedRankingDTOMapper.toNumberedRankingDTOs(numberedRankingService
      .getAllNumberedRankingsByUser(userId));
  }

  /**
   * Returns a numbered ranking by id and userId.
   * @param rankingId the id of the ranking to be fetched.
   * @param userId the owner of the ranking to be fetched
   * @return the numbered ranking as a DTO
   * @throws AccessDeniedException if the current authenticated user is not authorised to access this numbered ranking
   */
  @GetMapping("/users/{userId}/numberedrankings/{rankingId}")
  public NumberedRankingDTO getRanking(@PathVariable("rankingId") Long rankingId, @PathVariable("userId") Long userId) throws AccessDeniedException {
    return NumberedRankingDTOMapper.toNumberedRankingDTO(numberedRankingService.getNumberedRankingByUserAndId(rankingId, userId));
  }

  /**
   * Creates a new numbered ranking.
   * @param userId the id of the user creating the ranking
   * @param rankingDTO the DTO of ranking information used to create the numbered ranking. See {@link NumberedRankingDTO}.
   * @throws AccessDeniedException if the currently authenticated user does not have permission to create a numbered ranking.
   * Either because they are not authenticated or because they are trying to create a ranking for a different user.
   */
  //todo should service method have id seperate as well?
  @PostMapping("/users/{userId}/numberedrankings")
  public void createRanking(@PathVariable("userId") Long userId, @RequestBody @Valid NumberedRankingDTO rankingDTO) throws AccessDeniedException {
    if (!userId.equals(rankingDTO.getUserDTO().getId())) {
      throw new AccessDeniedException("User ID mismatch");
    }
    numberedRankingService.createNumberedRanking(rankingDTO);
  }

  /**
   * Updates a numbered ranking.
   * @param userId the id of the user that the ranking belongs to
   * @param rankingDTO the new data used to update the numbered ranking. See {@link NumberedRankingDTO}.
   * @throws AccessDeniedException if the currently authenticated user does not have permission to update this ranking
   */
  @PutMapping("/users/{userId}/numberedrankings/{rankingId}")
  public void updateRanking(@PathVariable("userId") Long userId, @RequestBody @Valid NumberedRankingDTO rankingDTO) throws AccessDeniedException {
    if (!userId.equals(rankingDTO.getUserDTO().getId())) {
      throw new AccessDeniedException("UserId Mismatch");
    }
    numberedRankingService.updateNumberedRanking(rankingDTO);
  }

  /**
   * Deletes the numbered ranking
   * @param rankingId the id of the numbered ranking to be deleted
   * @param userId the id of the user to whom the numbered ranking belongs
   * @throws AccessDeniedException if the currently authenticated user does not have permission to delete this ranking
   */
  @DeleteMapping("/users/{userId}/numberedrankings/{rankingId}")
  public void deleteRanking(@PathVariable("rankingId") Long rankingId, @PathVariable("userId") Long userId) throws AccessDeniedException {
    numberedRankingService.deleteNumberedRankingByIdAndUser(rankingId, userId);
  }
}
