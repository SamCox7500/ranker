package com.samcox.ranker.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when trying to access a {@link MediaList} that does not exist.
 * <p>Has error status code 400 - bad request</p>
 * @see MediaList
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MediaListNotFoundException extends RuntimeException{
  /**
   * Constructor for the exception.
   * @param message the error message explaining why the exception was thrown.
   */
  public MediaListNotFoundException(String message) {
      super(message);
    }
}
