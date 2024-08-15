package com.samcox.ranker.user;

public class UsernameExistsException extends RuntimeException {
  public UsernameExistsException(String message) {
    super(message);
  }
}
