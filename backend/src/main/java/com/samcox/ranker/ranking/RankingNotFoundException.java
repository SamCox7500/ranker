package com.samcox.ranker.ranking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Runtime exception thrown when trying to access a ranking from the database that does not exist
 * <p>Has error status code 400</p>
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RankingNotFoundException extends RuntimeException {
  /**
   * Constructor for the exception.
   * @param message the message explaining why the exception was thrown.
   */
  public RankingNotFoundException(String message) {
      super(message);
    }
}
