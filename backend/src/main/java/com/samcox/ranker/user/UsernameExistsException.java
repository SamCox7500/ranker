package com.samcox.ranker.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Runtime exception thrown when a request is made to create a new {@link User}
 * when a User with that username already exists.
 *<p>Has error status code 403 - forbidden</p>
 *
 * @see User
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UsernameExistsException extends RuntimeException {
  /**
   * Constructor for creating the exception object
   * @param message the error message explaining why the exception occurred.
   */
  public UsernameExistsException(String message) {
    super(message);
  }
}
