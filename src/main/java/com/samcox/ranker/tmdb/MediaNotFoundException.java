package com.samcox.ranker.tmdb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MediaNotFoundException extends IllegalArgumentException {
  public MediaNotFoundException(String message) {
    super(message);
  }
}
