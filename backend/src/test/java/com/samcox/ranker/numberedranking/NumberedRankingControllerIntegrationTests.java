package com.samcox.ranker.numberedranking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.media.*;
import com.samcox.ranker.numberedranking.*;
import com.samcox.ranker.ranking.MediaType;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class NumberedRankingControllerIntegrationTests {

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

  private MediaListEntry mediaListEntry;
  private MediaListEntry mediaListEntry1;
  private MediaListEntry mediaListEntry2;

  @BeforeEach
  public void setUp() {
    numberedRankingRepository.deleteAll();
    userRepository.deleteAll();

    testUser = new User();
    testUser.setUsername("testuser");
    testUser.setPassword(passwordEncoder.encode("Validpassword1!"));
    testUser.setRole("USER");
    userRepository.save(testUser);

    testUser1 = new User();
    testUser1.setUsername("testuser1");
    testUser1.setPassword(passwordEncoder.encode("Validpassword1!"));
    testUser1.setRole("USER");
    userRepository.save(testUser1);

    MediaList mediaList = new MediaList();

    testNumberedRanking = new NumberedRanking();
    testNumberedRanking.setUser(testUser);
    testNumberedRanking.setTitle("Valid title");
    testNumberedRanking.setDescription("Valid description");
    testNumberedRanking.setMediaType(MediaType.FILM);

    mediaListEntry = new MediaListEntry();
    mediaListEntry.setRanking(1);
    mediaListEntry.setTmdbId(7345L);
    mediaListEntry.setMediaList(mediaList);

    mediaListEntry1 = new MediaListEntry();
    mediaListEntry1.setRanking(2);
    mediaListEntry1.setTmdbId(115L);
    mediaListEntry1.setMediaList(mediaList);

    mediaListEntry2 = new MediaListEntry();
    mediaListEntry2.setRanking(3);
    mediaListEntry2.setTmdbId(4638L);
    mediaListEntry2.setMediaList(mediaList);

    mediaList.addEntry(mediaListEntry);
    mediaList.addEntry(mediaListEntry1);
    mediaList.addEntry(mediaListEntry2);

    testNumberedRanking.setMediaList(mediaList);

    numberedRankingRepository.save(testNumberedRanking);

  }
  @Test
  @WithMockUser("testuser")
  public void testGetRanking_Success() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title")
        .value("Valid title"))
      .andExpect(jsonPath("$.description")
        .value("Valid description"))
      .andExpect(jsonPath("$.mediaType")
        .value("FILM"))
      .andExpect(jsonPath("$.userDTO.username")
        .value("testuser"))
      .andExpect(jsonPath("$.userDTO.id")
        .value(testUser.getId()))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[0].ranking")
        .value(mediaListEntry.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[0].id")
        .value(mediaListEntry.getId()))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[0].title")
        .value("There Will Be Blood"))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[0].release_date")
        .value("2007-12-26"))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[0].poster_path")
        .value("/fa0RDkAlCec0STeMNAhPaF89q6U.jpg"))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[1].ranking")
        .value(mediaListEntry1.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[1].id")
        .value(mediaListEntry1.getId()))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[1].title")
        .value("The Big Lebowski"))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[1].release_date")
        .value("1998-03-06"))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[1].poster_path")
        .value("/9mprbw31MGdd66LR0AQKoDMoFRv.jpg"))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[2].ranking")
        .value(mediaListEntry2.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[2].id")
        .value(mediaListEntry2.getId()))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[2].title")
        .value("Hot Fuzz"))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[2].release_date")
        .value("2007-02-14"))
      .andExpect(jsonPath("$.mediaListDTO.mediaListEntryDTOList[2].poster_path")
        .value("/zPib4ukTSdXvHP9pxGkFCe34f3y.jpg"));

  }

  @Test
  @WithMockUser("testuser1")
  public void testGetRanking_NotAuthorized() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser("testuser")
  public void testGetRanking_InvalidRankingId() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings/" + 999L)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser("testuser")
  public void testCreateRanking_Success() throws Exception {

    UserDTO userDTO = new UserDTO(testUser.getId(), testUser.getUsername());

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("New ranking");
    createNumberedRankingDTO.setDescription("New desc");
    createNumberedRankingDTO.setMediaType("FILM");

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createNumberedRankingDTO)))
      .andExpect(status().isOk());

    Optional<List<NumberedRanking>> numberedRankings = numberedRankingRepository.findByUser(testUser);
    assertTrue(numberedRankings.isPresent());
    List<NumberedRanking> numberedRankingList = numberedRankings.get();

    NumberedRanking createdNumberedRanking = numberedRankingList.get(1);

    assertEquals(testUser.getId(), createdNumberedRanking.getUser().getId());
    assertEquals(createdNumberedRanking.getUser().getUsername(), testUser.getUsername());
    assertEquals(createNumberedRankingDTO.getTitle(), createdNumberedRanking.getTitle());
    assertEquals(createNumberedRankingDTO.getDescription(), createdNumberedRanking.getDescription());
    assertEquals(createNumberedRankingDTO.getMediaType(), createdNumberedRanking.getMediaType().toString());
  }

  @Test
  @WithMockUser("testuser1")
  public void testCreateRanking_NotAuthorized() throws Exception {

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("New ranking");
    createNumberedRankingDTO.setDescription("New desc");
    createNumberedRankingDTO.setMediaType("FILM");

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createNumberedRankingDTO)))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithAnonymousUser
  public void testCreateRanking_NotAuthenticated() throws Exception {

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("New ranking");
    createNumberedRankingDTO.setDescription("New desc");
    createNumberedRankingDTO.setMediaType("FILM");

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createNumberedRankingDTO)))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser")
  public void testCreateRanking_InvalidTitle() throws Exception {

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("");
    createNumberedRankingDTO.setDescription("New desc");
    createNumberedRankingDTO.setMediaType("FILM");

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createNumberedRankingDTO)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testCreateRanking_InvalidDesc() throws Exception {

    UserDTO userDTO = new UserDTO(testUser.getId(), testUser.getUsername());

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("New ranking");
    createNumberedRankingDTO.setDescription("");
    createNumberedRankingDTO.setMediaType("FILM");

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createNumberedRankingDTO)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testCreateRanking_InvalidMediaType() throws Exception {

    UserDTO userDTO = new UserDTO(testUser.getId(), testUser.getUsername());

    CreateNumberedRankingDTO createNumberedRankingDTO = new CreateNumberedRankingDTO();
    createNumberedRankingDTO.setTitle("New ranking");
    createNumberedRankingDTO.setDescription("Valid desc");
    createNumberedRankingDTO.setMediaType("INVALID");

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createNumberedRankingDTO)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testUpdateRanking_Success() throws Exception {

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("New title");
    updateNumberedRankingDTO.setDescription("New desc");

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateNumberedRankingDTO)))
      .andExpect(status().isOk());

    NumberedRanking updatedNumberedRanking = numberedRankingRepository.findById(testNumberedRanking.getId()).orElseThrow();


    assertEquals(testUser.getId(), updatedNumberedRanking.getUser().getId());
    assertEquals(testUser.getUsername(), updatedNumberedRanking.getUser().getUsername());
    assertEquals(updateNumberedRankingDTO.getTitle(), updatedNumberedRanking.getTitle());
    assertEquals(updateNumberedRankingDTO.getDescription(), updatedNumberedRanking.getDescription());
    assertEquals(testNumberedRanking.getMediaType().toString(), updatedNumberedRanking.getMediaType().toString());
  }
  @Test
  @WithMockUser("testuser1")
  public void testUpdateRanking_NotAuthorized() throws Exception {

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("New title");
    updateNumberedRankingDTO.setDescription("New desc");

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateNumberedRankingDTO)))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser")
  public void testUpdateRanking_InvalidId() throws Exception {

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("New title");
    updateNumberedRankingDTO.setDescription("New desc");

    //Numbered ranking does not exist with id 999
    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + 999L)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateNumberedRankingDTO)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testUpdateRanking_InvalidTitle() throws Exception {

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("");
    updateNumberedRankingDTO.setDescription("New desc");

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateNumberedRankingDTO)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testUpdateRanking_InvalidDesc() throws Exception {

    UpdateNumberedRankingDTO updateNumberedRankingDTO = new UpdateNumberedRankingDTO();
    updateNumberedRankingDTO.setTitle("New title");
    updateNumberedRankingDTO.setDescription("");

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(updateNumberedRankingDTO)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testDeleteRanking_Success() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());

    NumberedRanking deletedRanking = numberedRankingRepository.findById(testNumberedRanking.getId()).orElse(null);
    assertNull(deletedRanking);
  }
  @Test
  @WithMockUser(username = "testuser")
  public void testDeleteRanking_InvalidId() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + 999L)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser(username = "testuser1")
  public void testDeleteRanking_NotAuthorized() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser")
  public void testAddEntryToMediaList_Success() throws Exception {
    EntryAddRequest entryAddRequest = new EntryAddRequest();
    entryAddRequest.setTmdbId(299534L);
    entryAddRequest.setRanking(1);

    mockMvc.perform(post("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryAddRequest)))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testNumberedRanking.getMediaList().getId()).orElseThrow();
    assert(mediaList.getEntries().size() == 4);

    assertEquals(1, mediaList.getEntries().get(0).getRanking());
    assertEquals(299534L, mediaList.getEntries().get(0).getTmdbId());

    assertEquals(2, mediaList.getEntries().get(1).getRanking());
    assertEquals(7345L, mediaList.getEntries().get(1).getTmdbId());

    assertEquals(3, mediaList.getEntries().get(2).getRanking());
    assertEquals(115L, mediaList.getEntries().get(2).getTmdbId());

    assertEquals(4, mediaList.getEntries().get(3).getRanking());
    assertEquals(4638L, mediaList.getEntries().get(3).getTmdbId());
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


    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(mediaListEntry.getId(), 3);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + mediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testNumberedRanking.getMediaList().getId()).orElseThrow();


    assertEquals(mediaList.getEntries().get(0).getRanking(), 1);
    assertEquals(mediaList.getEntries().get(0).getTmdbId(), 115L);

    assertEquals(mediaList.getEntries().get(1).getRanking(), 2);
    assertEquals(mediaList.getEntries().get(1).getTmdbId(), 4638L);

    assertEquals(mediaList.getEntries().get(2).getRanking(), 3);
    assertEquals(mediaList.getEntries().get(2).getTmdbId(), 7345L);
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testMoveEntryInMediaListMultiple_Success() throws Exception {

    //current order 1: 7345 2: 115 3: 4638
    MediaList mediaList = mediaListRepository.findById(testNumberedRanking.getMediaList().getId()).orElseThrow();
    System.out.println("Initial media list state: " + mediaList.getEntries());

    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(mediaListEntry.getId(), 2);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + mediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isOk());

    System.out.println("Moved entry 1 to rank 2: " + mediaList.getEntries());

    EntryMoveRequest entryMoveRequest1 = new EntryMoveRequest(mediaListEntry1.getId(), 2);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + mediaListEntry1.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest1)))
      .andExpect(status().isOk());

    System.out.println("Moved entry 1 to rank 2: " + mediaList.getEntries());
  }
  @Test
  @WithMockUser("testuser1")
  public void testMoveEntryInMediaList_NotAuthorized() throws Exception {
    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(mediaListEntry.getId(), 3);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + mediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser")
  public void testMoveEntryInMediaList_InvalidRanking() throws Exception {

    EntryMoveRequest entryMoveRequest = new EntryMoveRequest(mediaListEntry.getId(), 4);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + mediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isBadRequest());

    entryMoveRequest.setNewPosition(0);

    mockMvc.perform(put("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + mediaListEntry.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryMoveRequest)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testRemoveEntryFromMediaList_Success() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + mediaListEntry1.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testNumberedRanking.getMediaList().getId()).orElseThrow();
    assertEquals(2, mediaList.getEntries().size());

    assertEquals(1, mediaList.getEntries().get(0).getRanking());
    assertEquals(7345L, mediaList.getEntries().get(0).getTmdbId());

    assertEquals(2, mediaList.getEntries().get(1).getRanking());
    assertEquals(4638L, mediaList.getEntries().get(1).getTmdbId());

  }
  @Test
  @WithMockUser("testuser1")
  public void testRemoveEntryFromMediaList_NotAuthorized() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries/" + mediaListEntry1.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
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
    entryIds.add(mediaListEntry.getId());
    entryIds.add(mediaListEntry2.getId());

    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryIds)))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testNumberedRanking.getMediaList().getId()).orElseThrow();
    assertEquals(1, mediaList.getEntries().size());

    assertEquals(mediaList.getEntries().get(0).getRanking(), 1);
    assertEquals(mediaList.getEntries().get(0).getTmdbId(), 115L);
  }
  @Test
  @WithMockUser("testuser1")
  public void testRemoveEntriesFromMediaList_NotAuthorized() throws Exception {
    List<Long> entryIds = new ArrayList<>();
    entryIds.add(mediaListEntry.getId());
    entryIds.add(mediaListEntry2.getId());

    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryIds)))
      .andExpect(status().isForbidden());
  }
  @Test
  @Transactional
  @WithMockUser("testuser")
  public void testRemoveEntriesFromMediaList_InvalidEntries() throws Exception {

    List<Long> entryIds = new ArrayList<>();
    entryIds.add(mediaListEntry.getId());
    entryIds.add(999L);

    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryIds)))
      .andExpect(status().isBadRequest());

    MediaList mediaList = mediaListRepository.findById(testNumberedRanking.getMediaList().getId()).orElseThrow();
    assertEquals(3, mediaList.getEntries().size());
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
    entryIds.add(mediaListEntry.getId());
    entryIds.add(mediaListEntry.getId());

    mockMvc.perform(delete("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId() + "/medialist" + "/entries")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(entryIds)))
      .andExpect(status().isOk());

    MediaList mediaList = mediaListRepository.findById(testNumberedRanking.getMediaList().getId()).orElseThrow();
    assertEquals(2, mediaList.getEntries().size());

    assertEquals(1, mediaList.getEntries().get(0).getRanking());
    assertEquals(115L, mediaList.getEntries().get(0).getTmdbId());

    assertEquals(2, mediaList.getEntries().get(1).getRanking());
    assertEquals(4638L, mediaList.getEntries().get(1).getTmdbId());
  }
}
