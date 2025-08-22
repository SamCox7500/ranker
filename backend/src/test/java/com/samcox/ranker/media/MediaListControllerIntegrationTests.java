package com.samcox.ranker.media;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingRepository;
import com.samcox.ranker.ranking.MediaType;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MediaListControllerIntegrationTests {

  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private AuthService authService;

  @Autowired
  private NumberedRankingRepository numberedRankingRepository;

  @Autowired
  private MediaListRepository mediaListRepository;

  private User testUser;
  private User testUser1;

  private NumberedRanking testNumberedRanking;

  private MediaList testMediaList;

  private MediaListEntry testMediaListEntry;
  private MediaListEntry testMediaListEntry1;
  private MediaListEntry testMediaListEntry2;

  @BeforeEach
  public void setUp() {
    mediaListRepository.deleteAll();
    numberedRankingRepository.deleteAll();
    userRepository.deleteAll();

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
  @AfterEach
  public void cleanUp() {
    mediaListRepository.deleteAll();
    numberedRankingRepository.deleteAll();
    userRepository.deleteAll();

  }
  @Test
  @WithMockUser("testuser")
  public void testGetMediaList_Success() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.mediaType")
        .value("FILM"))
      .andExpect(jsonPath("$.numberedRankingDTO.title").value(testNumberedRanking.getTitle()))
      .andExpect(jsonPath("$.numberedRankingDTO.description").value(testNumberedRanking.getDescription()))
      .andExpect(jsonPath("$.numberedRankingDTO.userDTO.username").value(testUser.getUsername()))
      .andExpect(jsonPath("$.mediaListEntryDTOList[0].ranking").value(testMediaListEntry.getRanking()))
      .andExpect(jsonPath("$.mediaListEntryDTOList[0].id").value(testMediaListEntry.getId()))
      .andExpect(jsonPath("$.mediaListEntryDTOList[0].title").value("There Will Be Blood"))
      .andExpect(jsonPath("$.mediaListEntryDTOList[0].release_date").value("2007-12-26"))
      .andExpect(jsonPath("$.mediaListEntryDTOList[0].poster_path").value("/nuZDiX8okojcwkStdaMjA9LUQAT.jpg"));
  }

  @Test
  @WithMockUser("testuser1")
  public void testGetMediaList_NotAuthorized() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser("testuser")
  public void testGetMediaList_InvalidUserId() throws Exception {
    mockMvc.perform(get("/users/" + 999L + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser")
  public void testGetMediaList_InvalidNumberedRankingId() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings/" + 999L + "/medialist")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testAddEntryToMediaList_Success() throws Exception {

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(1);

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryAddRequest)))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();
    assert(mediaList.getEntries().size() == 4);

    assertEquals(mediaList.getEntries().get(0).getRanking(), 1);
    assertEquals(mediaList.getEntries().get(0).getTmdbId(), 299534L);

    assertEquals(mediaList.getEntries().get(1).getRanking(), 2);
    assertEquals(mediaList.getEntries().get(1).getTmdbId(), 7345L);
    assertEquals(mediaList.getEntries().get(1).getMediaList().getId(), testMediaList.getId());

    assertEquals(mediaList.getEntries().get(2).getRanking(), 3);
    assertEquals(mediaList.getEntries().get(2).getTmdbId(), 115L);
    assertEquals(mediaList.getEntries().get(2).getMediaList().getId(), testMediaList.getId());

    assertEquals(mediaList.getEntries().get(3).getRanking(), 4);
    assertEquals(mediaList.getEntries().get(3).getTmdbId(), 4638L);
    assertEquals(mediaList.getEntries().get(3).getMediaList().getId(), testMediaList.getId());
  }
  @Test
  @WithMockUser("testuser1")
  public void testAddEntryToMediaList_NotAuthorized() throws Exception {
    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(1);

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryAddRequest)))
      .andExpect(status().isForbidden());
  }
  /*
  @Test
  @WithMockUser("testuser")
  public void testAddEntryToMediaList_InvalidMediaListId() throws Exception {
    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(1);

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist/" + 999L + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryAddRequest)))
      .andExpect(status().isBadRequest());
  }
  */
  @Test
  @WithMockUser("testuser")
  public void testAddEntryToMediaList_InvalidTmdb() throws Exception {
    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setRanking(1);

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryAddRequest)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntryToMediaList_InvalidRanking() throws Exception {

    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(0);

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryAddRequest)))
      .andExpect(status().isBadRequest());

    entryAddRequest.setRanking(5);

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" +  "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryAddRequest)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testMoveEntryInMediaList_Success() throws Exception {

    //current order 1: 7345 2: 115 3: 4638


    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(testMediaListEntry.getId(), 3);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + testMediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();


    assertEquals(mediaList.getEntries().get(0).getRanking(), 1);
    assertEquals(mediaList.getEntries().get(0).getTmdbId(), 115L);
    assertEquals(mediaList.getEntries().get(0).getMediaList().getId(), testMediaList.getId());

    assertEquals(mediaList.getEntries().get(1).getRanking(), 2);
    assertEquals(mediaList.getEntries().get(1).getTmdbId(), 4638L);
    assertEquals(mediaList.getEntries().get(1).getMediaList().getId(), testMediaList.getId());

    assertEquals(mediaList.getEntries().get(2).getRanking(), 3);
    assertEquals(mediaList.getEntries().get(2).getTmdbId(), 7345L);
    assertEquals(mediaList.getEntries().get(2).getMediaList().getId(), testMediaList.getId());
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testMoveEntryInMediaListMultiple_Success() throws Exception {
    //current order 1: 7345 2: 115 3: 4638
    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();
    System.out.println("Initial media list state: " + mediaList.getEntries());

    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(testMediaListEntry.getId(), 2);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + testMediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isOk());

    System.out.println("Moved entry 1 to rank 2: " + mediaList.getEntries());

    EntryMoveRequest entryMoveRequest1 = new EntryMoveRequest(testMediaListEntry1.getId(), 2);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + testMediaListEntry1.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest1)))
      .andExpect(status().isOk());

    System.out.println("Moved entry 1 to rank 2: " + mediaList.getEntries());
  }


  @Test
  @WithMockUser("testuser1")
  public void testMoveEntryInMediaList_NotAuthorized() throws Exception {
    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(testMediaListEntry.getId(), 3);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + testMediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isForbidden());
  }
  /*
  @Test
  @WithMockUser("testuser")
  public void testMoveEntryInMediaList_InvalidMediaListId() throws Exception {
    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(testMediaListEntry.getId(), 3);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist/" + 999L + "/entries/" + testMediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isBadRequest());
  }
   */
  @Test
  @WithMockUser("testuser")
  public void testMoveEntryInMediaList_InvalidRanking() throws Exception {

    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(testMediaListEntry.getId(), 4);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + testMediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isBadRequest());

    entryMoveRequest.setNewPosition(0);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + testMediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testRemoveEntryFromMediaList_Success() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + testMediaListEntry1.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();
    assertEquals(mediaList.getEntries().size(), 2);

    assertEquals(mediaList.getEntries().get(0).getRanking(), 1);
    assertEquals(mediaList.getEntries().get(0).getTmdbId(), 7345L);
    assertEquals(mediaList.getEntries().get(0).getMediaList().getId(), testMediaList.getId());

    assertEquals(mediaList.getEntries().get(1).getRanking(), 2);
    assertEquals(mediaList.getEntries().get(1).getTmdbId(), 4638L);
    assertEquals(mediaList.getEntries().get(1).getMediaList().getId(), testMediaList.getId());

  }
  @Test
  @WithMockUser("testuser1")
  public void testRemoveEntryFromMediaList_NotAuthorized() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + testMediaListEntry1.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
  /*
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntryFromMediaList_InvalidMediaListId() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist/" + 999L + "/entries/" + testMediaListEntry1.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
   */
  @Test
  @WithMockUser("testuser")
  public void testRemoveEntryFromMediaList_InvalidEntryId() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + 999L)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testRemoveEntriesFromMediaList_Success() throws Exception {
    List<Long> entryIds = new ArrayList<>();
    entryIds.add(testMediaListEntry.getId());
    entryIds.add(testMediaListEntry2.getId());

    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(entryIds)))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();
    assertEquals(mediaList.getEntries().size(), 1);

    assertEquals(mediaList.getEntries().get(0).getRanking(), 1);
    assertEquals(mediaList.getEntries().get(0).getTmdbId(), 115L);
    assertEquals(mediaList.getEntries().get(0).getMediaList().getId(), testMediaList.getId());
  }
  @Test
  @WithMockUser("testuser1")
  public void testRemoveEntriesFromMediaList_NotAuthorized() throws Exception {
    List<Long> entryIds = new ArrayList<>();
    entryIds.add(testMediaListEntry.getId());
    entryIds.add(testMediaListEntry2.getId());

    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryIds)))
      .andExpect(status().isForbidden());
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testRemoveEntriesFromMediaList_InvalidEntries() throws Exception {

    //System.out.println("MediaListEntryID:" + testMediaListEntry.getId());
    //System.out.println("MediaListEntry1ID:" + testMediaListEntry1.getId());
    //System.out.println("MediaListEntry2ID:" + testMediaListEntry2.getId());

    List<Long> entryIds = new ArrayList<>();
    entryIds.add(testMediaListEntry.getId());
    entryIds.add(999L);

    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryIds)))
      .andExpect(status().isBadRequest());

    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();
    assertEquals(mediaList.getEntries().size(), 3);
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testRemoveEntriesFromMediaList_DuplicateEntries() throws Exception {

    //System.out.println("MediaListEntryID:" + testMediaListEntry.getId());
    //System.out.println("MediaListEntry1ID:" + testMediaListEntry1.getId());
    //System.out.println("MediaListEntry2ID:" + testMediaListEntry2.getId());

    //Duplicate deletion entries should just delete the entry and not throw exceptions

    List<Long> entryIds = new ArrayList<>();
    entryIds.add(testMediaListEntry.getId());
    entryIds.add(testMediaListEntry.getId());

    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryIds)))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testMediaList.getId()).orElseThrow();
    assertEquals(mediaList.getEntries().size(), 2);

    assertEquals(mediaList.getEntries().get(0).getRanking(), 1);
    assertEquals(mediaList.getEntries().get(0).getTmdbId(), 115L);
    assertEquals(mediaList.getEntries().get(0).getMediaList().getId(), testMediaList.getId());

    assertEquals(mediaList.getEntries().get(1).getRanking(), 2);
    assertEquals(mediaList.getEntries().get(1).getTmdbId(), 4638L);
    assertEquals(mediaList.getEntries().get(1).getMediaList().getId(), testMediaList.getId());
  }
}
