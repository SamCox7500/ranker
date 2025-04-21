package com.samcox.ranker.tmdb;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samcox.ranker.media.TVShowDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TmdbControllerIntegrationTests {
  @Autowired
  private MockMvc mockMvc;
  @Autowired
  private ObjectMapper objectMapper;

  @Test
  public void testGetFilmDetails_Success() throws Exception {
    long thereWillBeBloodId = 7345L;

    mockMvc.perform(get("/tmdb/movies/" + thereWillBeBloodId)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.title").value("There Will Be Blood"))
      .andExpect(jsonPath("$.poster_path").value("/fa0RDkAlCec0STeMNAhPaF89q6U.jpg"))
      .andExpect(jsonPath("$.release_date").value("2007-12-26"));

  }
  @Test
  public void testGetFilmDetails_InvalidId() throws Exception {
    long invalidId = 9999999999L;

    mockMvc.perform(get("/tmdb/movies/" + invalidId)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }
  @Test
  public void testGetTVShowDetails_Success() throws Exception {
    long gameOfThronesId = 1399L;

    mockMvc.perform(get("/tmdb/tv/" + gameOfThronesId)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.name").value("Game of Thrones"))
      .andExpect(jsonPath("$.poster_path").value("/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg"))
      .andExpect(jsonPath("$.number_of_seasons").value("8"))
      .andExpect(jsonPath("$.number_of_episodes").value("73"))
      .andExpect(jsonPath("$.first_air_date").value("2011-04-17"))
      .andExpect(jsonPath("$.last_air_date").value("2019-05-19"));
  }
  @Test
  public void testGetTVShowDetails_InvalidId() throws Exception {
    long invalidId = 999999999999L;

    mockMvc.perform(get("/tmdb/tv/" + invalidId)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isNotFound());
  }
  @Test
  public void testFilmSearch() throws Exception {
    String query = "There Will Be ";

    mockMvc.perform(get("/tmdb/movies/search")
        .param("query", query)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.results[0].title").value("There Will Be Blood"))
      .andExpect(jsonPath("$.results[0].poster_path").value("/fa0RDkAlCec0STeMNAhPaF89q6U.jpg"))
      .andExpect(jsonPath("$.results[0].release_date").value("2007-12-26"));
  }
  @Test
  public void testTVShowSearch() throws Exception {
    String query = "Game of  ";

    mockMvc.perform(get("/tmdb/tv/search")
        .param("query", query)
        .contentType(org.springframework.http.MediaType.APPLICATION_JSON))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.results[0].name").value("Game of Thrones"))
      .andExpect(jsonPath("$.results[0].poster_path").value("/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg"))
      .andExpect(jsonPath("$.results[0].first_air_date").value("2011-04-17"));
  }
}
