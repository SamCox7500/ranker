package com.samcox.ranker.media;



import java.util.List;
import java.util.stream.Collectors;

public class MediaListEntryDTOMapper {

  public static MediaListEntryDTO toMediaListEntryDTO(MediaListEntry mediaListEntry) {
    return new MediaListEntryDTO(
      mediaListEntry.getId(),
      mediaListEntry.getRanking(),
      mediaListEntry.getTmdbId());
  }
  public static List<MediaListEntryDTO> toMediaListEntryDTOs(List<MediaListEntry> mediaListEntries) {
    return mediaListEntries.stream()
      .map(MediaListEntryDTOMapper::toMediaListEntryDTO)
      .collect(Collectors.toList());
  }
}
