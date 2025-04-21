package com.samcox.ranker.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Runtime exception to be thrown when the user tries to authenticate but is already authenticated.
 * <p>Has error status code 403 - forbidden.</p>
 */
@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAlreadyLoggedInException extends RuntimeException {
  /**
   * Constructor for creating exception object.
   * @param message The error message for the exception
   */
  public UserAlreadyLoggedInException(String message) {
    super(message);
  }
}
