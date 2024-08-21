package com.samcox.ranker.media;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.List;

@Entity
public class FilmList extends MediaList {

  @OneToMany(mappedBy = "filmList", cascade = CascadeType.ALL)
  private List<Film> films;

}
