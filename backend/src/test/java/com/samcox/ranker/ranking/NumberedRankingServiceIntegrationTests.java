package com.samcox.ranker.ranking;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.numberedranking.*;
import com.samcox.ranker.user.*;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class NumberedRankingServiceIntegrationTests {
  @Autowired
  NumberedRankingService numberedRankingService;

  @Autowired
  UserService userService;

  @Autowired
  AuthService authService;

  @Autowired
  NumberedRankingRepository numberedRankingRepository;

  @Autowired
  UserRepository userRepository;

  @Autowired
  PasswordEncoder passwordEncoder;

  @Autowired
  AuthenticationManager authenticationManager;

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

    //UserCredentials userCredentials = new UserCredentials();
    //userCredentials.setUsername("testuser");
    //userCredentials.setPassword("Validpassword1!");

    //Authenticating user for testing methods that require authentication
    /*
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      userCredentials.getUsername(), userCredentials.getPassword());
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);
     */
  }
  @Test
  @WithMockUser("testuser")
  public void testGetNumberedRankingByUserAndId_Success() throws AccessDeniedException {
    NumberedRanking numberedRanking = numberedRankingService.getNumberedRankingByIdAndUser(testNumberedRanking.getId(), testUser.getId());

    assertNotNull(numberedRanking);
    assertEquals(testNumberedRanking.getId(), numberedRanking.getId());
    assertEquals(testNumberedRanking.getUser(), numberedRanking.getUser());
    assertEquals(testNumberedRanking.getMediaType(), numberedRanking.getMediaType());
    assertEquals(testNumberedRanking.getMediaList(), numberedRanking.getMediaList());
    assertEquals(testNumberedRanking.getTitle(), numberedRanking.getTitle());
    assertEquals(testNumberedRanking.getDescription(), numberedRanking.getDescription());
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetNumberedRankingByUserAndId_NotAuthorized() throws AccessDeniedException {
    //A user attempting to gain access to a different user's ranking.
    assertThrows(AccessDeniedException.class,
      () -> numberedRankingService.getNumberedRankingByIdAndUser(testNumberedRanking.getId(), testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testGetNumberedRankingByUserAndId_InvalidRanking() throws AccessDeniedException {
    assertThrows(RankingNotFoundException.class,
      () -> numberedRankingService.getNumberedRankingByIdAndUser(900L, testUser.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testGetAllNumberedRankingsByUser_Success() throws AccessDeniedException {
    List<NumberedRanking> numberedRankings = numberedRankingService.getAllNumberedRankingsByUser(testUser.getId());
    assert(numberedRankings.size() == 1);
    assert(numberedRankings.get(0) == testNumberedRanking);
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetAllNumberedRankingsByUser_NotAuthorized() throws AccessDeniedException {
    assertThrows(AccessDeniedException.class,
      () -> numberedRankingService.getAllNumberedRankingsByUser(testUser.getId()));
  }
  @Test
  @WithMockUser("invaliduser")
  public void testGetAllNumberedRankingsByUser_InvalidUser() throws UserNotFoundException {
    assertThrows(UserNotFoundException.class,
      () -> numberedRankingService.getAllNumberedRankingsByUser(900L));
  }
  @Test
  @WithMockUser("testuser")
  public void testCreateNumberedRanking_Success() throws AccessDeniedException {
    Long userId = testUser.getId();

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("Test title");
    createNumberedRankingDTO.setDescription("Test description");
    createNumberedRankingDTO.setMediaType("FILM");

    numberedRankingService.createNumberedRanking(userId, createNumberedRankingDTO);

    List<NumberedRanking> numberedRankings = numberedRankingRepository.findByUser(testUser).orElseThrow();
    NumberedRanking createdNumberedRanking = numberedRankings.get(1);

    assertEquals(createdNumberedRanking.getUser().getId(), userId);
    assertEquals(createdNumberedRanking.getTitle(), createNumberedRankingDTO.getTitle());
    assertEquals(createdNumberedRanking.getDescription(), createNumberedRankingDTO.getDescription());
    assertEquals(createdNumberedRanking.getMediaType().toString(), createNumberedRankingDTO.getMediaType());
  }
  @Test
  @WithMockUser("testuser1")
  public void testCreateNumberedRanking_NotAuthorized() throws AccessDeniedException {
    Long testUserId = testUser.getId();

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("Test title");
    createNumberedRankingDTO.setDescription("Test description");
    createNumberedRankingDTO.setMediaType("FILM");

    assertThrows(AccessDeniedException.class,
      () -> numberedRankingService.createNumberedRanking(testUserId, createNumberedRankingDTO));
  }
  @Test
  @WithMockUser("testuser")
  public void testCreateNumberedRanking_InvalidTitle() throws AccessDeniedException {
    Long testUserId = testUser.getId();

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("");
    createNumberedRankingDTO.setDescription("Test description");
    createNumberedRankingDTO.setMediaType("FILM");

    assertThrows(RuntimeException.class,
      () -> numberedRankingService.createNumberedRanking(testUserId, createNumberedRankingDTO));
  }
  @Test
  @WithMockUser("testuser")
  public void testCreateNumberedRanking_InvalidDesc() throws AccessDeniedException {
    Long testUserId = testUser.getId();

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("Test title");
    createNumberedRankingDTO.setDescription("");
    createNumberedRankingDTO.setMediaType("FILM");

    assertThrows(RuntimeException.class,
      () -> numberedRankingService.createNumberedRanking(testUserId, createNumberedRankingDTO));
  }
  @Test
  @WithMockUser("testuser")
  public void testCreateNumberedRanking_InvalidMediaType() throws AccessDeniedException {
    Long testUserId = testUser.getId();

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("Test title");
    createNumberedRankingDTO.setDescription("Test description");
    createNumberedRankingDTO.setMediaType("invalid");

    assertThrows(RuntimeException.class,
      () -> numberedRankingService.createNumberedRanking(testUserId, createNumberedRankingDTO));
  }
  @Test
  @WithMockUser("testuser")
  public void testUpdateNumberedRanking_Success() throws AccessDeniedException {
    Long testUserId = testUser.getId();
    Long rankingId = testNumberedRanking.getId();

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("This is a new valid title");
    updateNumberedRankingDTO.setDescription("This is a new valid title");

    numberedRankingService.updateNumberedRanking(rankingId, testUserId, updateNumberedRankingDTO);

    NumberedRanking changedNumberedRanking = numberedRankingRepository.findById(testNumberedRanking.getId()).orElseThrow();
    assertEquals(changedNumberedRanking, testNumberedRanking);
  }
  @Test
  @WithMockUser("testuser1")
  public void testUpdateNumberedRanking_NotAuthorized() throws AccessDeniedException {
    Long userId = testUser.getId();
    Long rankingId = testNumberedRanking.getId();

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("This is a new valid title");
    updateNumberedRankingDTO.setDescription("This is a new description");

    assertThrows(AccessDeniedException.class,
      () -> numberedRankingService.updateNumberedRanking(rankingId, userId, updateNumberedRankingDTO));
  }
  @Test
  @WithMockUser("testuser")
  public void testUpdateNumberedRanking_InvalidTitle() throws AccessDeniedException {
    Long userId = testUser.getId();
    Long rankingId = testNumberedRanking.getId();

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("");
    updateNumberedRankingDTO.setDescription("This is a new description");


    assertThrows(RuntimeException.class,
      () -> numberedRankingService.updateNumberedRanking(rankingId, userId, updateNumberedRankingDTO));
  }
  @Test
  @WithMockUser("testuser")
  public void tesUpdateNumberedRanking_InvalidDesc() throws AccessDeniedException {
    Long userId = testUser.getId();
    Long rankingId = testNumberedRanking.getId();

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("This is a new valid title");
    updateNumberedRankingDTO.setDescription("");


    assertThrows(RuntimeException.class,
      () -> numberedRankingService.updateNumberedRanking(rankingId, userId, updateNumberedRankingDTO));
  }

  @Test
  @WithMockUser("testuser")
  public void testDeleteNumberedRankingByIdAndUser_Success() throws AccessDeniedException {
    numberedRankingService.deleteNumberedRankingByIdAndUser(testNumberedRanking.getId(), testUser.getId());
    Optional<NumberedRanking> numberedRanking = numberedRankingRepository.findById(testNumberedRanking.getId());
    assertTrue(numberedRanking.isEmpty());
  }
  @Test
  @WithMockUser("testuser1")
  public void testDeleteNumberedRankingByIdAndUser_NotAuthorized() throws AccessDeniedException {
    assertThrows(AccessDeniedException.class, () -> numberedRankingService.deleteNumberedRankingByIdAndUser(testNumberedRanking.getId(), testUser.getId()));
    assertThrows(AccessDeniedException.class, () -> numberedRankingService.deleteNumberedRankingByIdAndUser(testNumberedRanking.getId(), testUser1.getId()));
  }
}
