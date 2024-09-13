package com.samcox.ranker.tmdb;

import com.samcox.ranker.media.FilmDTO;
import com.samcox.ranker.media.TVShowDTO;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.HttpClientErrorException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
public class TmdbServiceIntegrationTests {
  @Autowired
  TmdbService tmdbService;

  @Test
  public void testGetFilmDetails_Success() {
    Long thereWillBeBloodId = 7345L;
    FilmDTO filmDTO = tmdbService.getFilmDetails(thereWillBeBloodId);

    assertEquals(filmDTO.getTitle(),"There Will Be Blood");
    assertEquals(filmDTO.getPoster_path(), "/fa0RDkAlCec0STeMNAhPaF89q6U.jpg");
    assertEquals(filmDTO.getReleaseDate(), "2007-12-26");

  }
  @Test
  public void testGetFilmDetails_InvalidId() {
    Long invalidFilmId = 9999999999999999L;
    assertThrows(HttpClientErrorException.class, () -> tmdbService.getFilmDetails(invalidFilmId));
  }
  @Test
  public void testGetTVShowDetails_Success() {
    Long gameOfThronesId = 1399L;
    TVShowDTO tvShowDTO = tmdbService.getTVShowDetails(gameOfThronesId);

    assertEquals(tvShowDTO.getName(),"Game of Thrones");
    assertEquals(tvShowDTO.getPoster_path(), "/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg");
    assertEquals(tvShowDTO.getNumber_of_seasons(), "8");
    assertEquals(tvShowDTO.getNumber_of_episodes(), "73");
    assertEquals(tvShowDTO.getFirst_air_date(), "2011-04-17");
    assertEquals(tvShowDTO.getLast_air_date(), "2019-05-19");
  }
  @Test
  public void testGetTVShowDetails_InvalidId() {
    Long invalidShowId = 9999999999999999L;
    assertThrows(HttpClientErrorException.class, () -> tmdbService.getTVShowDetails(invalidShowId));
  }
}
