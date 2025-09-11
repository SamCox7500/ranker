package com.samcox.ranker.ranking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.numberedranking.NumberedRanking;
import com.samcox.ranker.numberedranking.NumberedRankingRepository;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
public class RankingControllerIntegrationTests {

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

  private User testUser;
  private User testUser1;

  private NumberedRanking testNumberedRanking;

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
    testNumberedRanking.setMediaList(mediaList);
    testNumberedRanking.setMediaType(MediaType.FILM);
    testNumberedRanking.setRankingType(RankingType.NUMBERED_RANKING);

    numberedRankingRepository.save(testNumberedRanking);

  }
  @Test
  @WithMockUser("testuser")
  public void testGetRanking_Success() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/rankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title")
        .value("Valid title"))
      .andExpect(jsonPath("$.description")
        .value("Valid description"))
      .andExpect(jsonPath("$.mediaType")
        .value("FILM"))
      .andExpect(jsonPath("$.rankingType")
        .value("NUMBERED_RANKING"))
      .andExpect(jsonPath("$.userDTO.username")
        .value("testuser"))
      .andExpect(jsonPath("$.userDTO.id")
        .value(testUser.getId()));
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetRanking_NotAuthorized() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/rankings/" + testNumberedRanking.getId())
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser("testuser")
  public void testGetRanking_InvalidRankingId() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/rankings/" + 999L)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser("testuser")
  public void testGetAllRankings_Success() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/rankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].title")
        .value("Valid title"))
      .andExpect(jsonPath("$[0].description")
        .value("Valid description"))
      .andExpect(jsonPath("$[0].mediaType")
        .value("FILM"))
      .andExpect(jsonPath("$[0].rankingType")
        .value("NUMBERED_RANKING"))
      .andExpect(jsonPath("$[0].userDTO.username")
        .value("testuser"))
      .andExpect(jsonPath("$[0]userDTO.id")
        .value(testUser.getId()));
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetAllRankings_NotAuthorized() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/rankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithMockUser("testuser1")
  public void testGetAllRankings_NoRankings() throws Exception {
    //testUser1 has no rankings but the request should still return ok, just no data is returned.
    mockMvc.perform(get("/users/" + testUser1.getId() + "/rankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }
  @Test
  @WithMockUser("testuser")
  public void testGetAllRankings_InvalidId() throws Exception {
    mockMvc.perform(get("/users/" + 999L + "/rankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
}
