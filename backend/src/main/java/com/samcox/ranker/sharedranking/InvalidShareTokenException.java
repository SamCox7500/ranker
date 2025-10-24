package com.samcox.ranker.sharedranking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception thrown when trying to access a shared ranking with an invalid share token.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class InvalidShareTokenException extends RuntimeException {
  /**
   * Constructor for exception.
   * @param message message explaining why the exception was thrown.
   */
  public InvalidShareTokenException(String message) {
    super(message);
  }
}
