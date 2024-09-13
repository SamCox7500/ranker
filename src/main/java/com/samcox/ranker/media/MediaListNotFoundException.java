package com.samcox.ranker.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MediaListNotFoundException extends RuntimeException{
    public MediaListNotFoundException(String message) {
      super(message);
    }
}
