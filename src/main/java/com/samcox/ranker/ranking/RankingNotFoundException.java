package com.samcox.ranker.ranking;

public class RankingNotFoundException extends RuntimeException {
    public RankingNotFoundException(String message) {
      super(message);
    }
}
