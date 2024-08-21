package com.samcox.ranker.media;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;

import java.util.ArrayList;
import java.util.List;

@Entity
public class FilmList extends MediaList {

  @OneToMany(mappedBy = "filmList", cascade = CascadeType.ALL, orphanRemoval = true)
  @OrderBy("orderIndex ASC")
  private List<FilmListEntry> rankedFilms = new ArrayList<>();

  public List<FilmListEntry> getRankedFilms() {
    return rankedFilms;
  }
  public void setRankedFilms(List<FilmListEntry> rankedFilms) {
    this.rankedFilms = rankedFilms;
  }
}
