package com.samcox.ranker.media;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class DuplicateMediaEntryException extends IllegalArgumentException {
    public DuplicateMediaEntryException(String message) {
      super(message);
    }
}
