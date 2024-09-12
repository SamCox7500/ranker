package com.samcox.ranker.media;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.ranking.NumberedRanking;
import com.samcox.ranker.ranking.NumberedRankingRepository;
import com.samcox.ranker.ranking.NumberedRankingService;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import com.samcox.ranker.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
public class MediaListServiceIntegrationTests {

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

  @Autowired
  MediaListService mediaListService;

  @Autowired
  MediaListRepository mediaListRepository;

  private User testUser;
  private User testUser1;
  private NumberedRanking testNumberedRanking;

  private MediaList testMediaList;

  @BeforeEach
  public void setUp() {
    testUser = new User("testuser", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser);

    testUser1 = new User("testuser1", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser1);


    MediaList mediaList = new MediaList();
    mediaList.setMediaType(MediaType.FILM);

    testNumberedRanking = new NumberedRanking();
    testNumberedRanking.setUser(testUser);
    testNumberedRanking.setTitle("This is a test title");
    testNumberedRanking.setDescription("This is a test desc of a numbered ranking");
    testNumberedRanking.setMediaType(MediaType.FILM);
    testNumberedRanking.setMediaList(mediaList);

    numberedRankingRepository.save(testNumberedRanking);

    /*
    testMediaList = new MediaList();
    testMediaList.setMediaType(MediaType.FILM);
    testMediaList.setNumberedRanking(testNumberedRanking);
    List<MediaListEntry> testMediaListEntries = new ArrayList<>();
    testMediaList.setEntries(testMediaListEntries);

    mediaListRepository.save(testMediaList);
     */


    /*testMediaList = new MediaList();
    testMediaList.setMediaType(MediaType.FILM);
    testMediaList.setNumberedRanking(testNumberedRanking);
     */
  }
  @Test
  @WithMockUser("testuser")
  public void testGetMediaListByNumberedRankingAndUser_Success() throws AccessDeniedException {
    //MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(testNumberedRanking.getId(), testUser.getId());
    //assertNotNull(mediaList);

    //assertEquals(mediaList.getNumberedRanking().getId(), testNumberedRanking.getId());

    NumberedRanking numberedRanking = numberedRankingRepository.findById(testNumberedRanking.getId()).orElseThrow();
    System.out.println(numberedRanking.getId());
    System.out.println(numberedRanking.getMediaList().getId());
    MediaList mediaList = mediaListRepository.findById(1L).orElseThrow();
    System.out.println(mediaList.getId());
  }

  @WithMockUser("testuser1")
  public void testGetMediaListByNumberedRankingAndUser_NotAuthorized() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testGetMediaListByNumberedRankingAndUser_InvalidRanking() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testGetMediaListByNumberedRankingAndUser_InvalidUser() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntry_Success() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser1")
  public void testMoveEntry_NotAuthorized() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntry_InvalidMediaListId() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntry_InvalidOldPosition() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntry_InvalidNewPosition() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntry_Success() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser1")
  public void testRemoveEntry_NotAuthorized() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntry_InvalidEntry() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntries_Success() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser1")
  public void testRemoveEntries_NotAuthorized() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntries_InvalidEntries() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntries_NoEntriesSpecified() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntries_InvalidEntryListId() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_Success() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser1")
  public void testAddEntry_NotAuthorized() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_InvalidMediaListId() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_NullTmdbId() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_InvalidRanking() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_DuplicateMediaId() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser")
  public void checkOwnership_Success() throws AccessDeniedException {
  }
  @Test
  @WithMockUser("testuser1")
  public void checkOwnership_InvalidOwner() throws AccessDeniedException {
  }
}
