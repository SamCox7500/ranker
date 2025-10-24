package com.samcox.ranker.sharedranking;

import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.media.MediaListEntry;
import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingRepository;
import com.samcox.ranker.ranking.MediaType;
import com.samcox.ranker.ranking.Ranking;
import com.samcox.ranker.ranking.RankingNotFoundException;
import com.samcox.ranker.ranking.RankingType;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import jakarta.transaction.Transactional;
import net.bytebuddy.pool.TypePool;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.file.AccessDeniedException;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest
public class SharedRankingServiceIntegrationTests {

  @Autowired
  SharedRankingService sharedRankingService;

  @Autowired
  UserRepository userRepository;

  @Autowired
  NumberedRankingRepository numberedRankingRepository;

  @Autowired
  SharedRankingRepository sharedRankingRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  private User testUser;
  private User testUser1;
  private NumberedRanking testNumberedRanking;

  private MediaList mediaList;
  private MediaListEntry mediaListEntry;
  private MediaListEntry mediaListEntry1;
  private MediaListEntry mediaListEntry2;

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
    testNumberedRanking.setMediaType(MediaType.MOVIE);
    testNumberedRanking.setRankingType(RankingType.NUMBERED_RANKING);

    mediaList = new MediaList();

    mediaListEntry = new MediaListEntry();
    mediaListEntry.setRanking(1);
    mediaListEntry.setTmdbId(1078605L);
    mediaListEntry.setMediaList(mediaList);

    mediaListEntry1 = new MediaListEntry();
    mediaListEntry1.setRanking(2);
    mediaListEntry1.setTmdbId(550L);
    mediaListEntry1.setMediaList(mediaList);

    mediaListEntry2 = new MediaListEntry();
    mediaListEntry2.setRanking(3);
    mediaListEntry2.setTmdbId(105L);
    mediaListEntry2.setMediaList(mediaList);

    mediaList.addEntry(mediaListEntry);
    mediaList.addEntry(mediaListEntry1);
    mediaList.addEntry(mediaListEntry2);
    testNumberedRanking.setMediaList(mediaList);
    numberedRankingRepository.save(testNumberedRanking);

  }
  @Test
  @WithMockUser("testuser")
  public void testShareRanking_Success() throws AccessDeniedException {
    sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser.getId());
    SharedRanking sharedRanking = sharedRankingRepository.findByShareToken(testNumberedRanking.getSharedRanking().getShareToken()).orElseThrow();

    assertEquals(testNumberedRanking.getSharedRanking().getId(), sharedRanking.getId());
    assertEquals(testNumberedRanking.getSharedRanking().getShareToken(), sharedRanking.getShareToken());

    assertEquals(testNumberedRanking.getId(), sharedRanking.getRanking().getId());
    assertEquals(testNumberedRanking.getRankingType(), sharedRanking.getRanking().getRankingType());
    assertEquals(testNumberedRanking.getMediaType(), sharedRanking.getRanking().getMediaType());
    assertEquals(testNumberedRanking.getTitle(), sharedRanking.getRanking().getTitle());
    assertEquals(testNumberedRanking.getDescription(), sharedRanking.getRanking().getDescription());
    assertEquals(testNumberedRanking.getUser(), sharedRanking.getRanking().getUser());
  }
  @Test
  @WithMockUser("testuser1")
  public void testShareRanking_InvalidRankingAndUser() throws AccessDeniedException {
    assertThrows(RankingNotFoundException.class, () -> sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser1.getId()));
  }
  @Test
  @WithMockUser("testuser1")
  public void testShareRanking_Unauthorized() {
    assertThrows(AccessDeniedException.class, () -> sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testShareRanking_RankingAlreadyShared() throws AccessDeniedException {
    sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser.getId());
    assertThrows(RankingAlreadySharedException.class, () ->  sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testUnshareRanking_Success() throws AccessDeniedException {
    sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser.getId());

    String shareToken = testNumberedRanking.getSharedRanking().getShareToken();

    sharedRankingService.unshareRanking(testNumberedRanking.getId(), testUser.getId());

    assert(testNumberedRanking.getSharedRanking() == null);
    Optional<SharedRanking> sharedRanking = sharedRankingRepository.findByShareToken(shareToken);
    assert(sharedRanking.isEmpty());
  }
  @Test
  @WithMockUser("testuser")
  public void testUnshareRanking_RankingIsNotShared() throws AccessDeniedException {
    assertThrows(RankingNotSharedException.class, () -> sharedRankingService.unshareRanking(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser1")
  public void testUnshareRanking_Unauthorized() throws AccessDeniedException {
    assertThrows(AccessDeniedException.class, () -> sharedRankingService.unshareRanking(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testUnshareRanking_InvalidRankingIdAndUser() throws AccessDeniedException {
    assertThrows(RankingNotFoundException.class, () -> sharedRankingService.unshareRanking(999L, testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testIsShared_Success() throws AccessDeniedException {

    assert(!sharedRankingService.isShared(testNumberedRanking.getId(), testUser.getId()));
    sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser.getId());
    assert(sharedRankingService.isShared(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser1")
  public void testIsShared_Unauthorized() throws AccessDeniedException {
    assertThrows(AccessDeniedException.class, () -> sharedRankingService.isShared(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testIsShared_InvalidRankingAndUser() {
    assertThrows(RankingNotFoundException.class, () -> sharedRankingService.isShared(999L, testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testGetSharedRanking_Success() throws AccessDeniedException {
    sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser.getId());
    SharedRanking sharedRanking = sharedRankingService.getSharedRanking(testNumberedRanking.getId(), testUser.getId());

    assertEquals(testNumberedRanking.getSharedRanking().getId(), sharedRanking.getId());
    assertEquals(testNumberedRanking.getSharedRanking().getShareToken(), sharedRanking.getShareToken());

    Ranking ranking = sharedRanking.getRanking();
    assertEquals(ranking.getId(), testNumberedRanking.getId());
    assertEquals(ranking.getRankingType(), testNumberedRanking.getRankingType());
    assertEquals(ranking.getMediaType(), testNumberedRanking.getMediaType());
    assertEquals(ranking.getTitle(), testNumberedRanking.getTitle());
    assertEquals(ranking.getDescription(), testNumberedRanking.getDescription());
    assertEquals(ranking.getIsPublic(), testNumberedRanking.getIsPublic());
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetSharedRanking_Unauthorized() {
    assertThrows(AccessDeniedException.class, () -> sharedRankingService.getSharedRanking(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testGetSharedRanking_InvalidRankingAndUser() throws AccessDeniedException {
    assertThrows(RankingNotFoundException.class, () -> sharedRankingService.getSharedRanking(999L, testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testGetSharedRanking_RankingIsNotShared() {
    assertThrows(RankingNotSharedException.class, () -> sharedRankingService.getSharedRanking(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testViewSharedRanking_OwnerSuccess() throws AccessDeniedException {
    sharedRankingService.shareRanking(testNumberedRanking.getId(), testUser.getId());
    String shareToken = testNumberedRanking.getSharedRanking().getShareToken();
    Ranking ranking = sharedRankingService.viewSharedRanking(shareToken);
    assertEquals(ranking.getId(), testNumberedRanking.getId());
    assertEquals(ranking.getRankingType(), testNumberedRanking.getRankingType());
    assertEquals(ranking.getMediaType(), testNumberedRanking.getMediaType());
    assertEquals(ranking.getTitle(), testNumberedRanking.getTitle());
    assertEquals(ranking.getDescription(), testNumberedRanking.getDescription());
    assertEquals(ranking.getIsPublic(), testNumberedRanking.getIsPublic());
  }

  /**
   * Nested class for tests relating to guests accessing shared rankings by share token.
   * <p>Nested class is used because test methods require unique initialisation for the owner sharing the ranking,
   * and then the guest accessing it.</p>
   */
  @Nested
  class GuestTest {

    @BeforeEach
    public void guestSetUpSharing() throws AccessDeniedException {

      SharedRanking sharedRanking = new SharedRanking();
      sharedRanking.setShareToken(UUID.randomUUID().toString());
      sharedRanking.setRanking(testNumberedRanking);

      testNumberedRanking.setSharedRanking(sharedRanking);
      numberedRankingRepository.save(testNumberedRanking);
    }

    @Test
    @WithMockUser("testuser1")
    public void testViewSharedRanking_GuestSuccess() {
      String shareToken = testNumberedRanking.getSharedRanking().getShareToken();
      Ranking ranking = sharedRankingService.viewSharedRanking(shareToken);
      assertEquals(ranking.getId(), testNumberedRanking.getId());
      assertEquals(ranking.getRankingType(), testNumberedRanking.getRankingType());
      assertEquals(ranking.getMediaType(), testNumberedRanking.getMediaType());
      assertEquals(ranking.getTitle(), testNumberedRanking.getTitle());
      assertEquals(ranking.getDescription(), testNumberedRanking.getDescription());
      assertEquals(ranking.getIsPublic(), testNumberedRanking.getIsPublic());
    }
    @Test
    @WithMockUser("testuser1")
    public void testViewSharedRanking_InvalidShareToken() {
      assertThrows(InvalidShareTokenException.class, () -> sharedRankingService.viewSharedRanking("invalid token"));
    }
  }

}
