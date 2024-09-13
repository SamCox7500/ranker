package com.samcox.ranker.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RankingOutOfBoundsException extends IllegalArgumentException {
  public RankingOutOfBoundsException(String message) {
    super(message);
  }
}
