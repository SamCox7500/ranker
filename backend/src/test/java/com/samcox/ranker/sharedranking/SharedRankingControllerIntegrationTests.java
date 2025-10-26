package com.samcox.ranker.sharedranking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.media.MediaListEntry;
import com.samcox.ranker.media.MediaListRepository;
import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingRepository;
import com.samcox.ranker.ranking.MediaType;
import com.samcox.ranker.ranking.RankingRepository;
import com.samcox.ranker.ranking.RankingType;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class SharedRankingControllerIntegrationTests {

  @Autowired
  MockMvc mockMvc;
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
  private SharedRankingRepository sharedRankingRepository;

  @Autowired
  private RankingRepository rankingRepository;

  @Autowired
  private MediaListRepository mediaListRepository;

  private User testUser;
  private User testUser1;

  private NumberedRanking testNumberedRanking;
  private NumberedRanking testNumberedRanking1;

  private MediaListEntry mediaListEntry;
  private MediaListEntry mediaListEntry1;
  private MediaListEntry mediaListEntry2;
  private MediaListEntry mediaListEntry3;
  private MediaListEntry mediaListEntry4;
  private MediaListEntry mediaListEntry5;

  private SharedRanking testSharedRanking;

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
    testNumberedRanking.setMediaType(MediaType.MOVIE);
    testNumberedRanking.setRankingType(RankingType.NUMBERED_RANKING);
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

    MediaList mediaList1 = new MediaList();
    testNumberedRanking1 = new NumberedRanking();
    testNumberedRanking1.setUser(testUser1);
    testNumberedRanking1.setTitle("Valid title 2");
    testNumberedRanking1.setDescription("Valid description 2");
    testNumberedRanking1.setMediaType(MediaType.MOVIE);
    testNumberedRanking1.setRankingType(RankingType.NUMBERED_RANKING);
    mediaListEntry3 = new MediaListEntry();
    mediaListEntry3.setRanking(1);
    mediaListEntry3.setTmdbId(7345L);
    mediaListEntry3.setMediaList(mediaList);
    mediaListEntry4 = new MediaListEntry();
    mediaListEntry4.setRanking(2);
    mediaListEntry4.setTmdbId(115L);
    mediaListEntry4.setMediaList(mediaList);
    mediaListEntry5 = new MediaListEntry();
    mediaListEntry5.setRanking(3);
    mediaListEntry5.setTmdbId(4638L);
    mediaListEntry5.setMediaList(mediaList);
    mediaList1.addEntry(mediaListEntry3);
    mediaList1.addEntry(mediaListEntry4);
    mediaList1.addEntry(mediaListEntry5);
    testNumberedRanking1.setMediaList(mediaList1);


    testSharedRanking = new SharedRanking();
    testSharedRanking.setShareToken(UUID.randomUUID().toString());
    testSharedRanking.setRanking(testNumberedRanking1);

    testNumberedRanking1.setSharedRanking(testSharedRanking);
    numberedRankingRepository.save(testNumberedRanking1);
    sharedRankingRepository.save(testSharedRanking);
  }
  @Test
  @WithMockUser("testuser")
  public void testShareRanking_Success() throws Exception {
    mockMvc.perform(post("/users/" + testUser.getId() + "/rankings/" + testNumberedRanking.getId() + "/shared"))
      .andExpect(status().isOk());

    SharedRanking sharedRanking = rankingRepository.findByIdAndUser(testNumberedRanking.getId(), testUser).orElseThrow().getSharedRanking();
    assertEquals(sharedRanking.getId(), testNumberedRanking.getSharedRanking().getId());
    assertEquals(sharedRanking.getShareToken(), testNumberedRanking.getSharedRanking().getShareToken());
    assertEquals(sharedRanking.getRanking().getId(), testNumberedRanking.getId());
  }
  @Test
  @WithMockUser("testuser1")
  public void testShareRanking_Unauthorized() throws Exception {
    mockMvc.perform(post("/users/" + testUser.getId() + "/rankings/" + testNumberedRanking.getId() + "/shared"))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser1")
  public void testShareRanking_RankingAlreadyShared() throws Exception {
    mockMvc.perform(post("/users/" + testUser1.getId() + "/rankings/" + testNumberedRanking1.getId() + "/shared"))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testShareRanking_InvalidRankingAndUser() throws Exception {
    mockMvc.perform(post("/users/" + testUser.getId() + "/rankings/" + 999L + "/shared"))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser1")
  public void testUnshareRanking_Success() throws Exception {

    String shareToken = testSharedRanking.getShareToken();

    mockMvc.perform(delete("/users/" + testUser1.getId() + "/rankings/" + testNumberedRanking1.getId() + "/shared"))
      .andExpect(status().isOk());

    Optional<SharedRanking> sharedRanking = sharedRankingRepository.findByShareToken(shareToken);
    assert(sharedRanking.isEmpty());
  }
  @Test
  @WithMockUser("testuser")
  public void testUnshareRanking_Unauthorized() throws Exception {
    mockMvc.perform(delete("/users/" + testUser1.getId() + "/rankings/" + testNumberedRanking1.getId() + "/shared"))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser")
  public void testUnshareRanking_RankingNotShared() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/rankings/" + testNumberedRanking.getId() + "/shared"))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testUnshareRanking_InvalidRankingAndUser() throws Exception {
    mockMvc.perform(delete("/users/" + testUser.getId() + "/rankings/" + 999L + "/shared"))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetShareInfo_Success() throws Exception {
    mockMvc.perform(get("/users/" + testUser1.getId() + "/rankings/" +  testNumberedRanking1.getId() + "/shared"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.sharedToken")
        .value(testSharedRanking.getShareToken()));
  }
  @Test
  @WithMockUser("testuser")
  public void testGetShareInfo_Unauthorized() throws Exception {
    mockMvc.perform(get("/users/" + testUser1.getId() + "/rankings/" +  testNumberedRanking1.getId() + "/shared"))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser")
  public void testGetShareInfo_InvalidRankingAndUser() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/rankings/" +  999L + "/shared"))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testGetShareInfo_RankingNotShared() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/rankings/" +   testNumberedRanking.getId() + "/shared"))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser1")
  public void testViewSharedRanking_OwnerSuccess() throws Exception {
    mockMvc.perform(get("/sharedrankings/" + testSharedRanking.getShareToken()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title")
        .value("Valid title 2"))
      .andExpect(jsonPath("$.description")
        .value("Valid description 2"))
      .andExpect(jsonPath("$.mediaType")
        .value("MOVIE"))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].ranking")
        .value(mediaListEntry3.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].id")
        .value(mediaListEntry3.getId()))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].title")
        .value("There Will Be Blood"))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].release_date")
        .value("2007-12-26"))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].poster_path")
        .value("/fa0RDkAlCec0STeMNAhPaF89q6U.jpg"))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].ranking")
        .value(mediaListEntry4.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].id")
        .value(mediaListEntry4.getId()))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].title")
        .value("The Big Lebowski"))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].release_date")
        .value("1998-03-06"))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].poster_path")
        .value("/9mprbw31MGdd66LR0AQKoDMoFRv.jpg"))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].ranking")
        .value(mediaListEntry5.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].id")
        .value(mediaListEntry5.getId()))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].title")
        .value("Hot Fuzz"))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].release_date")
        .value("2007-02-14"))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].poster_path")
        .value("/zPib4ukTSdXvHP9pxGkFCe34f3y.jpg"));;
  }
  @Test
  @WithMockUser("testuser")
  public void testViewSharedRanking_GuestSuccess() throws Exception {
    mockMvc.perform(get("/sharedrankings/" + testSharedRanking.getShareToken()))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title")
        .value("Valid title 2"))
      .andExpect(jsonPath("$.description")
        .value("Valid description 2"))
      .andExpect(jsonPath("$.mediaType")
        .value("MOVIE"))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].ranking")
        .value(mediaListEntry3.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].id")
        .value(mediaListEntry3.getId()))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].title")
        .value("There Will Be Blood"))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].release_date")
        .value("2007-12-26"))
      .andExpect(jsonPath("$.mediaListDTO.entries[0].poster_path")
        .value("/fa0RDkAlCec0STeMNAhPaF89q6U.jpg"))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].ranking")
        .value(mediaListEntry4.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].id")
        .value(mediaListEntry4.getId()))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].title")
        .value("The Big Lebowski"))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].release_date")
        .value("1998-03-06"))
      .andExpect(jsonPath("$.mediaListDTO.entries[1].poster_path")
        .value("/9mprbw31MGdd66LR0AQKoDMoFRv.jpg"))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].ranking")
        .value(mediaListEntry5.getRanking()))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].id")
        .value(mediaListEntry5.getId()))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].title")
        .value("Hot Fuzz"))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].release_date")
        .value("2007-02-14"))
      .andExpect(jsonPath("$.mediaListDTO.entries[2].poster_path")
        .value("/zPib4ukTSdXvHP9pxGkFCe34f3y.jpg"));;
  }
  @Test
  @WithMockUser("testuser")
  public void testViewSharedRanking_InvalidShareToken() throws Exception {
    mockMvc.perform(get("/sharedrankings/" + 999L))
      .andExpect(status().isBadRequest());
  }
}
