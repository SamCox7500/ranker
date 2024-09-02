package com.samcox.ranker.media;

public class FilmDTO extends MediaListEntryDTO {
  private String releaseDate;

  public FilmDTO(Long id, Long tmdbId, int ranking, String title, MediaType mediaType, String releaseDate) {
    super(id, tmdbId, ranking, title, mediaType);
    this.releaseDate = releaseDate;
  }
}
