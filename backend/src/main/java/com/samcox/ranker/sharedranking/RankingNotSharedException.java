package com.samcox.ranker.sharedranking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when trying to unshare a ranking that has not been shared.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RankingNotSharedException extends RuntimeException {
  /**
   * Constructor for exception.
   *
   * @param message message explaining why the exception was thrown.
   */
  public RankingNotSharedException(String message) {
    super(message);
  }
}
