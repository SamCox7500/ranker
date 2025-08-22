package com.samcox.ranker.media;

import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingRepository;
import com.samcox.ranker.numberedranking.NumberedRankingService;
import com.samcox.ranker.ranking.*;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import com.samcox.ranker.user.UserService;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolationException;
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

import static org.junit.jupiter.api.Assertions.*;

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

  @Autowired
  MediaListEntryRepository mediaListEntryRepository;

  private User testUser;
  private User testUser1;
  private NumberedRanking testNumberedRanking;

  private MediaList testMediaList;

  private MediaListEntry testMediaListEntry;
  private MediaListEntry testMediaListEntry1;
  private MediaListEntry testMediaListEntry2;


  @BeforeEach
  public void setUp() {
    testUser = new User("testuser", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser);

    testUser1 = new User("testuser1", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(testUser1);


    testMediaListEntry = new MediaListEntry();
    testMediaListEntry.setTmdbId(7345L);
    testMediaListEntry.setRanking(1);
    testMediaListEntry1 = new MediaListEntry();
    testMediaListEntry1.setTmdbId(115L);
    testMediaListEntry1.setRanking(2);
    testMediaListEntry2 = new MediaListEntry();
    testMediaListEntry2.setTmdbId(4638L);
    testMediaListEntry2.setRanking(3);


    testMediaList = new MediaList();
    testMediaList.setMediaType(MediaType.FILM);
    testMediaList.addEntry(testMediaListEntry);
    testMediaList.addEntry(testMediaListEntry1);
    testMediaList.addEntry(testMediaListEntry2);

    testNumberedRanking = new NumberedRanking();
    testNumberedRanking.setUser(testUser);
    testNumberedRanking.setTitle("This is a test title");
    testNumberedRanking.setDescription("This is a test desc of a numbered ranking");
    testNumberedRanking.setMediaType(MediaType.FILM);
    testNumberedRanking.setMediaList(testMediaList);

    numberedRankingRepository.save(testNumberedRanking);
  }
  /*
  @Test
  @WithMockUser("testuser")
  public void testGetMediaListByNumberedRankingAndUser_Success() throws AccessDeniedException {
    MediaList mediaList = mediaListService.getMediaListByNumberedRankingAndUser(testNumberedRanking.getId(), testUser.getId());
    assertNotNull(mediaList);

    assertEquals(mediaList.getId(), testNumberedRanking.getMediaList().getId());

    assertEquals(testMediaList.getMediaType(), mediaList.getMediaType());


    List<MediaListEntry> entries = mediaList.getEntries();
    assertEquals(3, entries.size());
    assertEquals(entries.get(0), testMediaList.getEntries().get(0));
    assertEquals(entries.get(1), testMediaList.getEntries().get(1));
    assertEquals(entries.get(2), testMediaList.getEntries().get(2));

  }
  */
  /*
  @Test
  @WithMockUser("testuser1")
  public void testGetMediaListByNumberedRankingAndUser_NotAuthorized() throws AccessDeniedException {
    assertThrows(AccessDeniedException.class,
      () -> mediaListService.getMediaListByNumberedRankingAndUser(testNumberedRanking.getId(), testUser.getId()));
  }
   */
  /*
  @Test
  @WithMockUser("testuser")
  public void testGetMediaListByNumberedRankingAndUser_InvalidRanking() throws AccessDeniedException {
    assertThrows(RankingNotFoundException.class,
      () -> mediaListService.getMediaListByNumberedRankingAndUser(999L, testUser.getId()));
  }
   */
  /*
  @Test
  @WithMockUser("testuser")
  public void testGetMediaListByNumberedRankingAndUser_InvalidUser() throws AccessDeniedException {
    assertThrows(AccessDeniedException.class,
      () -> mediaListService.getMediaListByNumberedRankingAndUser(testNumberedRanking.getId(), 999L));
  }
   */
  @Test
  @WithMockUser("testuser")
  public void testMoveEntry_Success() throws AccessDeniedException {
    Long userId = testUser.getId();
    //Number 1 in list should become number 2, number 2 should become number 1
    mediaListService.moveEntryInList(userId, testMediaList.getId(), 1, 2);
    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();

    assertEquals(1, mediaList.getEntries().get(0).getRanking());
    assertEquals(115L, mediaList.getEntries().get(0).getTmdbId());
    assertEquals(testMediaList.getId(), mediaList.getEntries().get(0).getMediaList().getId());

    assertEquals(2, mediaList.getEntries().get(1).getRanking());
    assertEquals(7345L, mediaList.getEntries().get(1).getTmdbId());
    assertEquals(testMediaList.getId(), mediaList.getEntries().get(1).getMediaList().getId());

    assertEquals(3, mediaList.getEntries().get(2).getRanking());
    assertEquals(4638L, mediaList.getEntries().get(2).getTmdbId());
    assertEquals(testMediaList.getId(), mediaList.getEntries().get(2).getMediaList().getId());

    MediaListEntry mediaListEntry = mediaListEntryRepository.findByMediaListAndId(mediaList, mediaList.getEntries().get(0).getId()).orElseThrow();
    assertEquals(1, mediaListEntry.getRanking());
    assertEquals(115L, mediaListEntry.getTmdbId());
    MediaListEntry mediaListEntry1 = mediaListEntryRepository.findByMediaListAndId(mediaList, mediaList.getEntries().get(1).getId()).orElseThrow();
    assertEquals(2, mediaListEntry1.getRanking());
    assertEquals(7345L, mediaListEntry1.getTmdbId());

  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntryMultiple_Success() throws AccessDeniedException {
    Long userId = testUser.getId();
    System.out.println("Initial media list state: " + testMediaList.getEntries());
    mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), 1, 2);
    System.out.println("Media list after swapping 1 and 2: " + testMediaList.getEntries());
    mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), 1, 2);
    System.out.println("Media list after swapping 1 and 2 again: " + testMediaList.getEntries());

    //Should be original order
    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();

    assertEquals(1, mediaList.getEntries().get(0).getRanking());
    assertEquals(7345L, mediaList.getEntries().get(0).getTmdbId());
    assertEquals(testMediaList.getId(), mediaList.getEntries().get(0).getMediaList().getId());

    assertEquals(2, mediaList.getEntries().get(1).getRanking());
    assertEquals(115L, mediaList.getEntries().get(1).getTmdbId());
    assertEquals(testMediaList.getId(), mediaList.getEntries().get(1).getMediaList().getId());

    assertEquals(3, mediaList.getEntries().get(2).getRanking());
    assertEquals(4638L, mediaList.getEntries().get(2).getTmdbId());
    assertEquals(testMediaList.getId(), mediaList.getEntries().get(2).getMediaList().getId());

  }
  @Test
  @WithMockUser("testuser1")
  public void testMoveEntry_NotAuthorized() throws AccessDeniedException {
    assertThrows(AccessDeniedException.class,
      () ->  mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), 1, 2));
  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntry_InvalidMediaListId() throws AccessDeniedException {
    assertThrows(MediaListNotFoundException.class,
      () ->  mediaListService.moveEntryInList(testUser.getId(),999L, 1, 2));
  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntry_InvalidOldPosition() throws AccessDeniedException {
    assertThrows(IllegalArgumentException.class, () ->  mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), 4, 1));
    assertThrows(IllegalArgumentException.class, () ->  mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), 0, 1));
    assertThrows(IllegalArgumentException.class, () ->  mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), -1, 2));
  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntry_InvalidNewPosition() throws AccessDeniedException {
    assertThrows(IllegalArgumentException.class, () ->  mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), 3, 4));
    assertThrows(IllegalArgumentException.class, () ->  mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), 3, 0));
    assertThrows(IllegalArgumentException.class, () ->  mediaListService.moveEntryInList(testUser.getId(), testMediaList.getId(), 2, -1));
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntry_Success() throws AccessDeniedException {
    Long userId = testUser.getId();
    mediaListService.removeEntryInList(testUser.getId(), testMediaList.getId(), testMediaListEntry.getId());

    List<MediaListEntry> entries = mediaListRepository.findById(testMediaList.getId()).get().getEntries();
    assertEquals(2, entries.size());

    assertEquals(115L, entries.get(0).getTmdbId());
    assertEquals(1, entries.get(0).getRanking());

    assertEquals(4638L, entries.get(1).getTmdbId());
    assertEquals(2, entries.get(1).getRanking());
  }
  @Test
  @WithMockUser("testuser1")
  public void testRemoveEntry_NotAuthorized() throws AccessDeniedException {
    Long userId = testUser.getId();
    assertThrows(AccessDeniedException.class, () ->  mediaListService.removeEntryInList(testUser.getId(), testMediaList.getId(), testMediaListEntry.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntry_InvalidEntry() throws AccessDeniedException {
    assertThrows(MediaListEntryNotFoundException.class, () ->  mediaListService.removeEntryInList(testUser.getId(), testMediaList.getId(), 999L));
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntries_Success() throws AccessDeniedException {

    Long userId = testUser.getId();

    List<Long> entriesToDelete = new ArrayList<>();
    entriesToDelete.add(testMediaListEntry.getId());
    entriesToDelete.add(testMediaListEntry2.getId());

    mediaListService.removeEntriesInList(userId, testMediaList.getId(), entriesToDelete);

    List<MediaListEntry> entries = mediaListRepository.findById(testMediaList.getId()).get().getEntries();
    assertEquals(1, entries.size());
    assertEquals(115L, entries.get(0).getTmdbId());
    assertEquals(1, entries.get(0).getRanking());
  }
  @Test
  @WithMockUser("testuser1")
  public void testRemoveEntries_NotAuthorized() throws AccessDeniedException {

    Long userId = testUser.getId();

    List<Long> entriesToDelete = new ArrayList<>();
    entriesToDelete.add(testMediaListEntry.getId());
    entriesToDelete.add(testMediaListEntry2.getId());

    assertThrows(AccessDeniedException.class, () ->  mediaListService.removeEntriesInList(userId, testMediaList.getId(), entriesToDelete));
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntries_InvalidEntries() throws AccessDeniedException {

    Long userId = testUser.getId();

    List<Long> entriesToDelete = new ArrayList<>();
    entriesToDelete.add(testMediaListEntry.getId());
    entriesToDelete.add(999L);

    assertThrows(MediaListEntryNotFoundException.class, () ->  mediaListService.removeEntriesInList(userId, testMediaList.getId(), entriesToDelete));
  }
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntries_NoEntriesSpecified() throws AccessDeniedException {

    Long userId = testUser.getId();

    List<Long> entriesToDelete = new ArrayList<>();
    assertThrows(MediaListEntryNotFoundException.class, () ->  mediaListService.removeEntriesInList(userId, testMediaList.getId(), entriesToDelete));
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_Success() throws AccessDeniedException {

    Long userId = testUser.getId();

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(2);

    mediaListService.addEntryToList(userId, entryAddRequest, testMediaList.getId());

    List<MediaListEntry> entries = mediaListRepository.findById(testMediaList.getId()).get().getEntries();
    assertEquals(4, entries.size());


    assertEquals(1, entries.get(0).getRanking());
    assertEquals(7345L, entries.get(0).getTmdbId());
    assertEquals(testMediaList.getId(), entries.get(0).getMediaList().getId());

    assertEquals(2, entries.get(1).getRanking());
    assertEquals(299534L, entries.get(1).getTmdbId());
    assertEquals(testMediaList.getId(), entries.get(1).getMediaList().getId());

    assertEquals(3, entries.get(2).getRanking());
    assertEquals(115L, entries.get(2).getTmdbId());
    assertEquals(testMediaList.getId(), entries.get(2).getMediaList().getId());

    assertEquals(4, entries.get(3).getRanking());
    assertEquals(4638L, entries.get(3).getTmdbId());
    assertEquals(testMediaList.getId(), entries.get(3).getMediaList().getId());

  }
  @Test
  @WithMockUser("testuser1")
  public void testAddEntry_NotAuthorized() throws AccessDeniedException {

    Long userId = testUser.getId();

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(2);

    assertThrows(AccessDeniedException.class, () ->  mediaListService.addEntryToList(userId, entryAddRequest, testMediaList.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_InvalidMediaListId() throws AccessDeniedException {

    Long userId = testUser.getId();

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(2);

    assertThrows(MediaListNotFoundException.class, () ->  mediaListService.addEntryToList(userId, entryAddRequest, 999L));
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_NullTmdbId() throws AccessDeniedException {

    Long userId = testUser.getId();

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    //entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(2);

    assertThrows(ConstraintViolationException.class, () ->  mediaListService.addEntryToList(userId, entryAddRequest, testMediaList.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_InvalidTmdbId() throws AccessDeniedException {
  //todo use tmdb service to check validity
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_InvalidRanking() throws AccessDeniedException {

    Long userId = testUser.getId();

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(5);

    assertThrows(IllegalArgumentException.class, () ->  mediaListService.addEntryToList(userId, entryAddRequest, testMediaList.getId()));
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntry_DuplicateMediaId() throws AccessDeniedException {

    Long userId = testUser.getId();

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(115L);
    entryAddRequest.setRanking(4);

    assertThrows(DuplicateMediaEntryException.class, () ->  mediaListService.addEntryToList(userId, entryAddRequest, testMediaList.getId()));
  }
}
