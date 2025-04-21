package com.samcox.ranker.tmdb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * IllegalArgumentException  thrown when attempting to access a TMDB movie or tv show that does not exist.
 * Using an invalid ID.
 * <p>Has error code status not found - 404</p>
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class MediaNotFoundException extends IllegalArgumentException {
  /**
   * Constructor for exception
   * @param message the message indicating why the error that has occurred
   */
  public MediaNotFoundException(String message) {
    super(message);
  }
}
