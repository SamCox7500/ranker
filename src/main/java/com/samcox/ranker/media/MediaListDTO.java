package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRankingDTO;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class MediaListDTO {
  private Long id;

  @NotNull
  private MediaType mediaType;

  @NotNull
  private List<MediaListEntryDTO> mediaListEntryDTOList;

  @NotNull
  private NumberedRankingDTO numberedRankingDTO;

  public MediaListDTO() {

  }

  public MediaListDTO(Long id, MediaType mediaType, List<MediaListEntryDTO> mediaListEntryDTOList, NumberedRankingDTO numberedRankingDTO) {
    this.id = id;
    this.mediaType = mediaType;
    this.mediaListEntryDTOList = mediaListEntryDTOList;
    this.numberedRankingDTO = numberedRankingDTO;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<MediaListEntryDTO> getMediaListEntryDTOList() {
    return mediaListEntryDTOList;
  }

  public void setMediaListEntryDTOList(List<MediaListEntryDTO> mediaListEntryDTOList) {
    this.mediaListEntryDTOList = mediaListEntryDTOList;
  }

  public MediaType getMediaType() {
    return mediaType;
  }

  public void setMediaType(MediaType mediaType) {
    this.mediaType = mediaType;
  }

  public NumberedRankingDTO getNumberedRankingDTO() {
    return numberedRankingDTO;
  }

  public void setNumberedRankingDTO(NumberedRankingDTO numberedRankingDTO) {
    this.numberedRankingDTO = numberedRankingDTO;
  }
}
