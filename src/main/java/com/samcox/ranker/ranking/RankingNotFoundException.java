package com.samcox.ranker.ranking;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class RankingNotFoundException extends RuntimeException {
    public RankingNotFoundException(String message) {
      super(message);
    }
}
