package com.samcox.ranker.sharedranking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when trying to share a ranking that has already been shared.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RankingAlreadySharedException extends RuntimeException {
  /**
   * Constructor for exception.
   *
   * @param message message explaining why the exception was thrown.
   */
  public RankingAlreadySharedException(String message) {
    super(message);
  }
}
