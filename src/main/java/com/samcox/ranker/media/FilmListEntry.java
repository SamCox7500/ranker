package com.samcox.ranker.media;

import jakarta.persistence.*;

@Entity
public class FilmListEntry {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "film_list_id")
  private FilmList filmList;

  @ManyToOne
  @JoinColumn(name = "film_id")
  private Film film;

  private int orderIndex;
}
