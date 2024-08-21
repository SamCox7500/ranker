package com.samcox.ranker.media;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Film {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private Long tmdbFilmId;
}
