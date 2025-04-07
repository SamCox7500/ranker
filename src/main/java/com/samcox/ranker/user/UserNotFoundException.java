package com.samcox.ranker.user;

/**
 * Runtime exception thrown when a {@link User} being searched for does not exist in the database
 * @see User
 */
public class UserNotFoundException extends RuntimeException {

  /**
   * Constructor for the exception
   * @param message the error message explaining why the exception was thrown
   */
  public UserNotFoundException(String message) {
    super(message);
  }
}
