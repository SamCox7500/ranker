package com.samcox.ranker.user;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class UserAlreadyLoggedInException extends RuntimeException {
  public UserAlreadyLoggedInException(String message) {
    super(message);
  }
}
