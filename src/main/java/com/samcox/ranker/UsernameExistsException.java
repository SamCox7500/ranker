package com.samcox.ranker;

public class UsernameExistsException extends RuntimeException {
  public UsernameExistsException(String message) {
    super(message);
  }
}
