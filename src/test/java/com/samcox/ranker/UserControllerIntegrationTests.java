package com.samcox.ranker;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcox.ranker.user.User;
import com.samcox.ranker.user.UserCredentials;
import com.samcox.ranker.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
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

  private User user;
  @BeforeEach
  public void setUp() {
    userRepository.deleteAll();
    user = new User();
    user.setUsername("testuser");
    user.setPassword(passwordEncoder.encode("Validpassword1!"));
    user.setRole("USER");
    userRepository.save(user);
  }
  //todo authorization
  @Test
  public void testGetUsers() throws Exception {
    mockMvc.perform(get("/users")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].username").value("testuser"));
  }
  @Test
  public void testGetUser() throws Exception {
    mockMvc.perform(get("/users/" + user.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.username").value("testuser"));
  }
  @Test
  public void testCreateUser() throws Exception {
    UserCredentials newUser = new UserCredentials();
    newUser.setUsername("newuser");
    newUser.setPassword("Validpassword1!");

    mockMvc.perform(post("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(newUser)))
      .andExpect(status().isOk());

    mockMvc.perform(get("/users")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[1].username").value("newuser"));
  }
  @Test
  public void testUpdateUser() throws Exception {
    UserCredentials newCredentials = new UserCredentials();
    newCredentials.setUsername("testuser");
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
  public void testDeleteUser() throws Exception {
    mockMvc.perform(delete("/users/" + user.getId())
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());

    User deletedUser = userRepository.findById(user.getId()).orElse(null);
    assertNull(deletedUser);
  }
}
