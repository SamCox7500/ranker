package com.samcox.ranker.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Thrown when a user tries to add a {@link MediaListEntry} to a {@link MediaList}
 * where the entry contains the same TMDB id as one already in the list.
 *
 * <p>
 *   Has error status code 400 - bad request.
 * </p>
 * @see MediaListEntry
 * @see MediaList
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateMediaEntryException extends IllegalArgumentException {
  /**
   * Constructor for exception.
   * @param message error message explaining why the exception was thrown
   */
  public DuplicateMediaEntryException(String message) {
      super(message);
    }
}
