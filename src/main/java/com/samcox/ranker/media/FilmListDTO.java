package com.samcox.ranker.media;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public class FilmListDTO {

  private Long id;
  @NotNull
  private List<FilmListEntryDTO> filmListEntryDTOS;


}
