package com.samcox.ranker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTests {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;
  @Autowired
  private CustomUserDetailsService customUserDetailsService;
  private User user;
  @BeforeEach
  public void setUp() {
    //Ensuring repository is empty before use
    userRepository.deleteAll();
    //Saving a user that has credentials that can be used to test login
    user = new User("testuser", passwordEncoder.encode("Validpassword1!"), "USER");
    userRepository.save(user);
  }

  @Test
  public void testLogin_Success() throws Exception {
    //Logging in with credentials valid to the test user.
    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("testuser");
    userCredentials.setPassword("Validpassword1!");
    mockMvc.perform(post("/login")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(userCredentials)))
      .andExpect(status().isOk());
  }
  @Test
  public void testLogin_InvalidCredentials() throws Exception {
    //Attempting to log in as the test user with the incorrect password
    UserCredentials invalidCredentials = new UserCredentials();
    invalidCredentials.setUsername("testuser");
    invalidCredentials.setPassword("Wrongpassword1!");
    mockMvc.perform(post("/login")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(invalidCredentials)))
      .andExpect(status().isUnauthorized());
  }
  //todo logout
  @Test
  @WithMockUser(username = "user", password = "password")
  public void testLogout_Success() throws Exception {
    //Simulating logging out as an authenticated user
    mockMvc.perform(post("/logout"))
      .andExpect(status().is2xxSuccessful())
      .andReturn();
  }
  //todo How to test that logout process works correctly.
}
