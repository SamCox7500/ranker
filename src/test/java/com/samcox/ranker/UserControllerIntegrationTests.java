package com.samcox.ranker;

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

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private AuthService authService;


  private User user;
  @BeforeEach
  public void setUp() {
    userRepository.deleteAll();
    user = new User();
    user.setUsername("testuser");
    user.setPassword(passwordEncoder.encode("Validpassword1!"));
    user.setRole("USER");
    userRepository.save(user);


    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("testuser");
    userCredentials.setPassword("Validpassword1!");

    SecurityContextHolder.clearContext();

    //Authenticating user for testing methods that require authentication
    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
      userCredentials.getUsername(), userCredentials.getPassword());
    Authentication authentication = authenticationManager.authenticate(authToken);
    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

  }
  //todo authorization
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
  public void testGetUser() throws Exception {
    mockMvc.perform(get("/users/" + user.getId())
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").value(user.getId()))
      .andExpect(jsonPath("$.username").value("testuser"));
  }
  @Test
  public void testCreateUser() throws Exception {
    //Clear auth as user cannot be created when already logged in
    SecurityContextHolder.clearContext();

    UserCredentials newUser = new UserCredentials();
    newUser.setUsername("newuser");
    newUser.setPassword("Validpassword1!");


    //UserDTO userDTO = authService.getAuthenticatedUser();
    //System.out.println(userDTO);

    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    System.out.println(auth.isAuthenticated());

    /*
    mockMvc.perform(post("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .content(objectMapper.writeValueAsString(newUser)))
      .andExpect(status().isOk());

    mockMvc.perform(get("/users")
      .contentType(MediaType.APPLICATION_JSON))
      .andExpect(jsonPath("$[1].username").value("newuser"));
     */
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
