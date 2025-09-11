package com.samcox.ranker.numberedranking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.media.MediaList;
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

    numberedRankingRepository.save(testNumberedRanking);

  }
  /*
  @Test
  @WithMockUser("testuser")
  public void testGetAllRankings_Success() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].title")
        .value("Valid title"))
      .andExpect(jsonPath("$[0].description")
        .value("Valid description"))
      .andExpect(jsonPath("$[0].mediaType")
        .value("FILM"))
      .andExpect(jsonPath("$[0].userDTO.username")
        .value("testuser"))
      .andExpect(jsonPath("$[0]userDTO.id")
        .value(testUser.getId()));
  }

  @Test
  @WithMockUser("testuser1")
  public void testGetAllRankings_NotAuthorized() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser("testuser1")
  public void testGetAllRankings_NoRankings() throws Exception {
    //testUser1 has no rankings but the request should still return ok, just no data is returned.
    mockMvc.perform(get("/users/" + testUser1.getId() + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  @WithMockUser("testuser")
  public void testGetAllRankings_InvalidId() throws Exception {
    mockMvc.perform(get("/users/" + 999L + "/numberedrankings")
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
   */
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
        .value(testUser.getId()));
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
  /*
  @Test
  @WithMockUser("testuser")
  public void testGetMediaList_Success() throws Exception {
    mockMvc.perform(get("/users/" + testUser.getId() + "/numberedrankings/" + testNumberedRanking.getId())
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
   */
}
