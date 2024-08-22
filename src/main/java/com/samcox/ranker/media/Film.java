package com.samcox.ranker.media;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;

@Entity
public class Film {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotBlank
  private Long tmdbFilmId;

  public Long getId() {
    return id;
  }

  public Long getTmdbFilmId() {
    return tmdbFilmId;
  }
}
