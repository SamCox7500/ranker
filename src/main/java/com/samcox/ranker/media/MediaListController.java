package com.samcox.ranker.media;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class MediaListController {

  private final MediaListService mediaListService;

  public MediaListController(MediaListService mediaListService) {
    this.mediaListService = mediaListService;
  }


}
