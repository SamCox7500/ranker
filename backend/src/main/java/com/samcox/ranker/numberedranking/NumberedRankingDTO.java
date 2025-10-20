package com.samcox.ranker.numberedranking;

import com.samcox.ranker.media.MediaListDTO;
import com.samcox.ranker.ranking.RankingDTO;
import com.samcox.ranker.user.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import javax.print.attribute.standard.Media;

/**
 * DTO for {@link NumberedRanking}.
 *
 * @see NumberedRanking
 * @see UserDTO
 * @see com.samcox.ranker.user.User
 */
public class NumberedRankingDTO extends RankingDTO {

  private boolean isReverseOrder;

  @NotNull(message = "NumberedRankingDTO must have a MediaListDTO")
  private MediaListDTO mediaListDTO;

  /**
   * Creates a numbered ranking DTO.
   * @param id the id of the numbered ranking
   * @param userDTO dto for the user who owns the numbered ranking
   * @param title the title of the numbered ranking
   * @param desc the description of the numbered ranking
   * @param isPublic if the numbered ranking is open to all or just the owner
   * @param isReverseOrder true if the numbered ranking is in descending order of ranked items. False if ascending order
   * @param mediaType the media type of the numbered ranking
   * @param rankingType the type of ranking e.g. numbered ranking, tier list
   */
  public NumberedRankingDTO(long id, UserDTO userDTO, String title, String desc, boolean isPublic, String mediaType, String rankingType, boolean isReverseOrder) {
    super(id, userDTO, title, desc, isPublic, rankingType, mediaType);
    this.isReverseOrder = isReverseOrder;
  }

  /**
   * Returns the media list as a dto
   * @return the media list of the numbered ranking as a dto
   */
  public MediaListDTO getMediaListDTO() {
    return mediaListDTO;
  }

  /**
   * Sets the media list dto of the numbered ranking dto.
   * @param mediaListDTO the dto representation of the media list belonging to the numbered ranking
   */
  public void setMediaListDTO(MediaListDTO mediaListDTO) {
    this.mediaListDTO = mediaListDTO;
  }

}
