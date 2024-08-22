package com.samcox.ranker.media;

import jakarta.validation.constraints.NotBlank;

public class FilmListEntryDTO {

  private Long id;
  @NotBlank
  private Long filmListId;
  @NotBlank
  private FilmDTO filmDTO;
  @NotBlank
  private int orderIndex;
}
