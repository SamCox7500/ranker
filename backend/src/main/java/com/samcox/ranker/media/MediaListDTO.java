package com.samcox.ranker.media;

import com.samcox.ranker.ranking.MediaType;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 *
 */
public class MediaListDTO {
  private Long id;

  @NotNull
  private MediaType mediaType;

  @NotNull
  private List<MediaListEntryDTO> entries;

  /*
  @NotNull
  private NumberedRankingDTO numberedRankingDTO;
   */

  public MediaListDTO() {

  }

  public MediaListDTO(Long id, MediaType mediaType, List<MediaListEntryDTO> entries) {
    this.id = id;
    this.mediaType = mediaType;
    this.entries = entries;
    //this.numberedRankingDTO = numberedRankingDTO;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public List<MediaListEntryDTO> getEntries() {
    return entries;
  }

  public void setEntries(List<MediaListEntryDTO> entries) {
    this.entries = entries;
  }
  /*
  public NumberedRankingDTO getNumberedRankingDTO() {
    return numberedRankingDTO;
  }

  public void setNumberedRankingDTO(NumberedRankingDTO numberedRankingDTO) {
    this.numberedRankingDTO = numberedRankingDTO;
  }
   */
}
