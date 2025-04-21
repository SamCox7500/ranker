package com.samcox.ranker.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcox.ranker.auth.AuthService;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserDTO;
import com.samcox.ranker.user.UserRepository;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTests {
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


  private User user;
  private User user1;
  @BeforeEach
  public void setUp() {
    userRepository.deleteAll();
    user = new User();
    user.setUsername("testuser");
    user.setPassword(passwordEncoder.encode("Validpassword1!"));
    user.setRole("USER");
    userRepository.save(user);

    user1 = new User();
    user1.setUsername("wronguser");
    user1.setPassword(passwordEncoder.encode("Validpassword1!"));
    user1.setRole("USER");
    userRepository.save(user1);

    SecurityContextHolder.clearContext();
  }
  /*
  @Test
  public void testGetUsers() throws Exception {
    mockMvc.perform(get("/users")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].username").value("testuser"));
  }
   */
  @Test
  @WithMockUser(username = "testuser", roles = "USER")
  public void testGetUser_Success() throws Exception {
    mockMvc.perform(get("/users/" + user.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(user.getId()))
      .andExpect(jsonPath("$.username").value("testuser"));
  }
  @Test
  @WithMockUser(username = "wronguser", roles = "USER")
  public void testGetUser_NotAuthorized() throws Exception {
    mockMvc.perform(get("/users/" + user.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithAnonymousUser
  public void testCreateUser() throws Exception {
    //Clear auth as user cannot be created when already logged in
    //SecurityContextHolder.clearContext();

    UserCredentials newUser = new UserCredentials();
    newUser.setUsername("newuser");
    newUser.setPassword("Validpassword1!");

    mockMvc.perform(post("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(newUser)))
      .andExpect(status().isOk());

    Optional<User> user = userRepository.findByUsername("newuser");
    assertTrue(user.isPresent());
  }
  @Test
  @WithMockUser()
  public void testCreateUser_AlreadyLoggedIn() throws Exception {

    UserCredentials newUser = new UserCredentials();
    newUser.setUsername("newuser");
    newUser.setPassword("Validpassword1!");

    mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(newUser)))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithAnonymousUser()
  public void testCreateUser_UsernameAlreadyExists() throws Exception {

    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("testuser");
    userCredentials.setPassword("Validpassword1!");

    mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentials)))
      .andExpect(status().isForbidden());
  }
  @Test
  @WithAnonymousUser()
  public void testCreateUser_InvalidUsername() throws Exception {

    UserCredentials userCredentials = new UserCredentials();
    //username too long
    userCredentials.setUsername("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    userCredentials.setPassword("Validpassword1!");

    mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentials)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithAnonymousUser()
  public void testCreateUser_InvalidPassword() throws Exception {

    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("validusername");
    userCredentials.setPassword("Invalidpassword1");

    mockMvc.perform(post("/users")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentials)))
      .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(username = "testuser")
  public void testUpdateUser_Success() throws Exception {
    UserCredentials newCredentials = new UserCredentials();
    newCredentials.setUsername("testuser");
    //The new password to be set
    newCredentials.setPassword("Newvalidpassword1!");

    mockMvc.perform(put("/users/" + user.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(newCredentials)))
      .andExpect(status().isOk());

    User updatedUser = userRepository.findById(user.getId()).orElse(null);
    assertNotNull(updatedUser);
    assertTrue(passwordEncoder.matches("Newvalidpassword1!", updatedUser.getPassword()));
  }
  @Test
  @WithMockUser(username = "testuser")
  public void testUpdateUser_InvalidPassword() throws Exception {
    UserCredentials newCredentials = new UserCredentials();
    newCredentials.setUsername("validusername");
    newCredentials.setPassword("Invalidpassword1");

    mockMvc.perform(put("/users/" + user.getId())
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(newCredentials)))
      .andExpect(status().isBadRequest());
  }
  @Test
  @WithMockUser(username = "testuser")
  public void testDeleteUser() throws Exception {
    mockMvc.perform(delete("/users/" + user.getId())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());

    User deletedUser = userRepository.findById(user.getId()).orElse(null);
    assertNull(deletedUser);
  }
  @Test
  @WithMockUser(username = "testuser")
  public void testDeleteUser_InvalidId() throws Exception {
    mockMvc.perform(delete("/users/" + 999L)
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isForbidden());
  }
}
