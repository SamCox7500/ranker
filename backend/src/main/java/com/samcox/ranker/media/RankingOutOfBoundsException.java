package com.samcox.ranker.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * An exception thrown when adding or moving a {@link MediaListEntry} in a {@link MediaList}
 * to a ranking that is out of bounds.
 * <p>Example: In a list of 3 entries, trying to move an entry to be ranked 4th</p>
 * <p>Has error status code 400 - bad request</p>
 * @see MediaListEntry
 * @see MediaList
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RankingOutOfBoundsException extends IllegalArgumentException {
  /**
   * Constructor for the exception.
   * @param message the error message explaining why the exception was thrown.
   */
  public RankingOutOfBoundsException(String message) {
    super(message);
  }
}
