package com.samcox.ranker.ranking;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingRepository;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import com.samcox.ranker.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.file.AccessDeniedException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class RankingServiceIntegrationTests {

  @Autowired
  RankingService rankingService;

  @Autowired
  RankingRepository rankingRepository;

  @Autowired
  UserService userService;

  @Autowired
  AuthService authService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  NumberedRankingRepository numberedRankingRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  private User testUser;
  private User testUser1;
  private NumberedRanking testNumberedRanking;

  private MediaList mediaList = new MediaList();

  @BeforeEach
  public void setUp() {
    testUser = new User("testuser", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser);

    testUser1 = new User("testuser1", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser1);

    testNumberedRanking = new NumberedRanking();
    testNumberedRanking.setUser(testUser);
    testNumberedRanking.setTitle("This is a test title");
    testNumberedRanking.setDescription("This is a test desc of a numbered ranking");
    testNumberedRanking.setMediaType(MediaType.FILM);
    testNumberedRanking.setMediaList(mediaList);

    numberedRankingRepository.save(testNumberedRanking);
  }

  @Test
  @WithMockUser("testuser")
  public void testGetRankingByIdAndUser_Success() throws AccessDeniedException {
    Ranking ranking = rankingService.getRankingByIdAndUser(testNumberedRanking.getId(), testUser.getId());

    assertNotNull(ranking);
    assertEquals(testNumberedRanking.getId(), ranking.getId());
    assertEquals(testNumberedRanking.getUser().getId(), ranking.getUser().getId());
    assertEquals(testNumberedRanking.getUser().getUsername(), ranking.getUser().getUsername());
    assertEquals(testNumberedRanking.getTitle(), ranking.getTitle());
    assertEquals(testNumberedRanking.getDescription(), ranking.getDescription());
    assertEquals(testNumberedRanking.getIsPublic(), ranking.getIsPublic());
    assertEquals(testNumberedRanking.getRankingType(), ranking.getRankingType());
    assertEquals(testNumberedRanking.getMediaType(), ranking.getMediaType());
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetRankingByIdAndUser_NotAuthorized() throws AccessDeniedException {
    assertThrows(AccessDeniedException.class,
      () -> rankingService.getRankingByIdAndUser(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testGetRankingByIdAndUser_InvalidRanking() {
    assertThrows(RankingNotFoundException.class,
      () -> rankingService.getRankingByIdAndUser(999L, testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testGetAllRankingsByUser_Success() throws AccessDeniedException {
    List<Ranking> rankings = rankingService.getAllRankingsByUser(testUser.getId());
    assertEquals(1, rankings.size());

    Ranking ranking = rankings.get(0);
    assertNotNull(ranking);
    assertEquals(testNumberedRanking.getId(), ranking.getId());
    assertEquals(testNumberedRanking.getUser().getId(), ranking.getUser().getId());
    assertEquals(testNumberedRanking.getUser().getUsername(), ranking.getUser().getUsername());
    assertEquals(testNumberedRanking.getTitle(), ranking.getTitle());
    assertEquals(testNumberedRanking.getDescription(), ranking.getDescription());
    assertEquals(testNumberedRanking.getIsPublic(), ranking.getIsPublic());
    assertEquals(testNumberedRanking.getRankingType(), ranking.getRankingType());
    assertEquals(testNumberedRanking.getMediaType(), ranking.getMediaType());
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetAllRankingsByUser_NotAuthorized() {
    assertThrows(AccessDeniedException.class,
      () -> rankingService.getAllRankingsByUser(testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void updateRanking_Success() throws AccessDeniedException {

    UpdateRankingDTO updateRankingDTO = new UpdateRankingDTO();
    updateRankingDTO.setTitle("New valid title");
    updateRankingDTO.setDescription("New valid description");
    updateRankingDTO.setPublic(true);

    rankingService.updateRanking(testNumberedRanking.getId(), testUser.getId(), updateRankingDTO);

    Ranking ranking = rankingService.getRankingByIdAndUser(testNumberedRanking.getId(), testUser.getId());
    assertNotNull(ranking);
    assertEquals(testNumberedRanking.getId(), ranking.getId());
    assertEquals(testNumberedRanking.getUser().getId(), ranking.getUser().getId());
    assertEquals(testNumberedRanking.getUser().getUsername(), ranking.getUser().getUsername());
    assertEquals(updateRankingDTO.getTitle(), ranking.getTitle());
    assertEquals(updateRankingDTO.getDescription(), ranking.getDescription());
    assertEquals(updateRankingDTO.isPublic(), ranking.getIsPublic());
    assertEquals(testNumberedRanking.getRankingType(), ranking.getRankingType());
    assertEquals(testNumberedRanking.getMediaType(), ranking.getMediaType());
  }
  @Test
  @WithMockUser("testuser1")
  public void updateRanking_NotAuthorized() throws AccessDeniedException {

    UpdateRankingDTO updateRankingDTO = new UpdateRankingDTO();
    updateRankingDTO.setTitle("New valid title");
    updateRankingDTO.setDescription("New valid description");
    updateRankingDTO.setPublic(true);

    assertThrows(AccessDeniedException.class,
      () -> rankingService.updateRanking(testNumberedRanking.getId(), testUser.getId(), updateRankingDTO));

  }
  @Test
  @WithMockUser("testuser")
  public void updateRanking_InvalidRanking() throws RankingNotFoundException {

    UpdateRankingDTO updateRankingDTO = new UpdateRankingDTO();
    updateRankingDTO.setTitle("New valid title");
    updateRankingDTO.setDescription("New valid description");
    updateRankingDTO.setPublic(true);

    assertThrows(RankingNotFoundException.class,
      () ->  rankingService.updateRanking(999L, testUser.getId(), updateRankingDTO));
  }
  @Test
  @WithMockUser("testuser")
  public void updateRanking_InvalidTitle() throws ConstraintViolationException {

    UpdateRankingDTO updateRankingDTO = new UpdateRankingDTO();
    updateRankingDTO.setTitle("");
    updateRankingDTO.setDescription("New valid description");
    updateRankingDTO.setPublic(true);

    assertThrows(ConstraintViolationException.class,
      () -> rankingService.updateRanking(testNumberedRanking.getId(), testUser.getId(), updateRankingDTO));
  }
  @Test
  @WithMockUser("testuser")
  public void updateRanking_InvalidDesc() throws ConstraintViolationException {

    UpdateRankingDTO updateRankingDTO = new UpdateRankingDTO();
    updateRankingDTO.setTitle("New valid title");
    updateRankingDTO.setDescription("");
    updateRankingDTO.setPublic(true);

    assertThrows(ConstraintViolationException.class,
      () -> rankingService.updateRanking(testNumberedRanking.getId(), testUser.getId(), updateRankingDTO));
  }
}
