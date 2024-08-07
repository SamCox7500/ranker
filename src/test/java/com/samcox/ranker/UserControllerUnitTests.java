package com.samcox.ranker;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class UserControllerUnitTests {
  @Mock
  public UserService userService;
  @InjectMocks
  public UserController userController;
  private MockMvc mockMvc;

  @BeforeEach
  public void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
  }
  @Test
  public void testGetUsers_Success() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(1L);
    userDTO.setUsername("testuser");
    when(userService.getAllUsers()).thenReturn(Collections.singletonList(userDTO));
    mockMvc.perform(get("/users"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$[0].username").value("testuser"));

    verify(userService, times(1)).getAllUsers();
  }
  @Test
  public void testGetUser_Success() throws Exception {
    UserDTO userDTO = new UserDTO();
    userDTO.setId(1L);
    userDTO.setUsername("testuser");
    when(userService.getUserByID(1L)).thenReturn(userDTO);
    mockMvc.perform(get("/users/1"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.username").value("testuser"));

    verify(userService, times(1)).getUserByID(1L);
  }
  @Test
  public void testCreateUser_Success() throws Exception {
    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("newuser");
    userCredentials.setPassword("Validpassword1!");

    ObjectMapper objectMapper = new ObjectMapper();
    String userCredentialsJson = objectMapper.writeValueAsString(userCredentials);

    mockMvc.perform(post("/users")
      .contentType(MediaType.APPLICATION_JSON)
      .content(userCredentialsJson))
      .andExpect(status().isOk());

    verify(userService, times(1)).createUser(any(UserCredentials.class));
  }
  @Test
  public void testUpdateUser_Success() throws Exception {
    UserCredentials userCredentials = new UserCredentials();
    userCredentials.setUsername("updateduser");
    userCredentials.setPassword("Validpassword1!");

    ObjectMapper objectMapper = new ObjectMapper();
    String userCredentialsJson = objectMapper.writeValueAsString(userCredentials);

    mockMvc.perform(put("/users/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(userCredentialsJson))
      .andExpect(status().isOk());

    verify(userService, times(1)).changePassword(eq(1L), any(UserCredentials.class));
  }
  @Test
  public void testDeleteUser_Success() throws Exception {
    mockMvc.perform(delete("/users/1"))
      .andExpect(status().isOk());

    verify(userService, times(1)).deleteUser(1L);
  }
}
