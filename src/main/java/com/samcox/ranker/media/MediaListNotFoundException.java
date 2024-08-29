package com.samcox.ranker.media;

public class MediaListNotFoundException extends RuntimeException{
    public MediaListNotFoundException(String message) {
      super(message);
    }
}
