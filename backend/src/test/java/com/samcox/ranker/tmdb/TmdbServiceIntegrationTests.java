package com.samcox.ranker.tmdb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/*
@SpringBootTest
@Transactional
public class TmdbServiceIntegrationTests {
  @Autowired
  TmdbService tmdbService;

  @Test
  public void testGetFilmDetails_Success() {
    Long thereWillBeBloodId = 7345L;
    MovieDTO filmDTO = tmdbService.getFilmDetails(thereWillBeBloodId);

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
  @Test
  public void testSearchFilms() {
    String query = "There Will Be Bloo";
    MovieSearchResultListDTO searchResultListDTO = tmdbService.searchMovies(query);

    MovieSearchResultDTO bestResultDTO = searchResultListDTO.getResults().get(0);
    assertEquals(bestResultDTO.title, "There Will Be Blood");
    assertEquals(bestResultDTO.getPoster_path(), "/fa0RDkAlCec0STeMNAhPaF89q6U.jpg");
    assertEquals(bestResultDTO.getRelease_date(), "2007-12-26");
    assertEquals(bestResultDTO.getId(), 7345L);
  }
  @Test
  public void testSearchShows() {
    String query = "Game Of ";
    TVShowSearchResultListDTO searchResultListDTO = tmdbService.searchTVShows(query);

    TVShowSearchResultDTO bestResultDTO = searchResultListDTO.getResults().get(0);
    assertEquals(bestResultDTO.getName(),"Game of Thrones");
    assertEquals(bestResultDTO.getPoster_path(), "/1XS1oqL89opfnbLl8WnZY1O1uJx.jpg");
    assertEquals(bestResultDTO.getFirst_air_date(), "2011-04-17");
  }
}
*/
