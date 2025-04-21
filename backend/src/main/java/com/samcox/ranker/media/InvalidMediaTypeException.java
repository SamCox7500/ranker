package com.samcox.ranker.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when trying to create a Ranking with a {@link MediaType} that is not known.
 *
 * <p>Has error status code 400 - bad request</p>
 *
 * @see MediaType
 *
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidMediaTypeException extends IllegalArgumentException {
  /**
   * Constructor for the exception
   * @param message the error message explaining why the exception was thrown
   */
  public InvalidMediaTypeException(String message) {
      super(message);
    }
}
