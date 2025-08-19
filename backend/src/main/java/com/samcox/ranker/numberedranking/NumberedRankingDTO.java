package com.samcox.ranker.numberedranking;

import com.samcox.ranker.user.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link NumberedRanking}.
 *
 * @see NumberedRanking
 * @see UserDTO
 * @see com.samcox.ranker.user.User
 */
public class NumberedRankingDTO {
  /**
   * Unique identifier for the numbered ranking.
   */
  private Long id;
  /**
   * DTO for the {@link com.samcox.ranker.user.User} that owns the numbered ranking. Must not be null.
   */
  @NotNull(message = "NumberedRankingDTO must have a user assigned to it")
  private UserDTO userDTO;
  /**
   * The title of the numbered ranking. Must be between 1 and 30 characters.
   */
  @NotBlank(message = "Ranking must have a title")
  @Size(min = 1, max = 30, message = "Ranking title must be between 1 and 30 chars")
  private String title;

  /**
   * The description of the numbered ranking. Must be between 1 and 150 characters.
   */
  @NotBlank(message = "Ranking must have a description")
  @Size(min = 1, max = 150, message = "Ranking description must be between 1 and 150 chars")
  private String description;

  /**
   * If the numbered ranking is open to all user or just the owner.
   */
  private boolean isPublic;

  /**
   * By default, the numbered ranking is in descending order from the number 1 ranked piece of media.
   * Reverse order would be ascending order.
   */
  private boolean isReverseOrder;

  /**
   * The media type of the numbered ranking. E.g., MOVIES, TV_SHOWS.
   */
  private String mediaType;

  /**
   * Default constructor required by JPA.
   */
  public NumberedRankingDTO() {}

  /**
   * Creates a numbered ranking DTO.
   * @param id the id of the numbered ranking
   * @param userDTO dto for the user who owns the numbered ranking
   * @param title the title of the numbered ranking
   * @param desc the description of the numbered ranking
   * @param isPublic if the numbered ranking is open to all or just the owner
   * @param isReverseOrder true if the numbered ranking is in descending order of ranked items. False if ascending order.
   * @param mediaType the media type of the numbered ranking
   */
  public NumberedRankingDTO(long id, UserDTO userDTO, String title, String desc, boolean isPublic, boolean isReverseOrder, String mediaType) {
    this.id = id;
    this.userDTO = userDTO;
    this.title = title;
    this.description = desc;
    this.isPublic = isPublic;
    this.isReverseOrder = isReverseOrder;
    this.mediaType = mediaType;
  }

  /**
   * Returns the id of the numbered ranking dto.
   * @return the id of the numbered ranking in the dto.
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the id for the numbered ranking represented by the dto.
   * @param id the id of the numbered ranking
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the {@link UserDTO} of the user that owns the numbered ranking.
   * @return the UserDTO of the user that owns the numbered ranking
   */
  public UserDTO getUserDTO() {
    return userDTO;
  }

  /**
   * Sets the UserDTO for the numbered ranking DTO.
   * @param userDTO the userDTO of the user to be set as the owner of the numbered ranking
   */
  public void setUserDTO(UserDTO userDTO) {
    this.userDTO = userDTO;
  }

  /**
   * Returns the title of the numbered ranking.
   * @return the title of the numbered ranking
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the numbered ranking.
   * @param title of the numbered ranking to be set
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the description of the numbered ranking.
   * @return the description of the numbered ranking
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description of the numbered ranking.
   * @param description the description of then numbered ranking
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns if the numbered ranking is public or not.
   * @return {@code true} if the numbered ranking is public. {@code false} otherwise.
   */
  public boolean isPublic() {
    return isPublic;
  }

  /**
   * Sets if the numbered ranking is public or not
   * @param isPublic {@code true} if the numbered ranking is toe be public, {@code false otherwise}.
   */
  public void setPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }

  /**
   * Returns if the numbered ranking is in descending order. Lowest ranked item to highest ranked item.
   * @return {@code true} if the list is in descending order. {@code false} if the list is in ascending order.
   */
  public boolean isReverseOrder() {
    return isReverseOrder;
  }

  /**
   * Sets if the numbered ranking is in reverse order.
   * @param reverseOrder {@code true if the numbered ranking list is to be reverse order, {@code false} otherwise.
   */
  public void setReverseOrder(boolean reverseOrder) {
    isReverseOrder = reverseOrder;
  }

  /**
   * Returns the media type of the numbered ranking.
   * @return string representation of the media type e.g., "TV_SHOW", "FILM".
   */
  public String getMediaType() {
    return mediaType;
  }

  /**
   * Sets the media type of the numbered ranking.
   * @param mediaType the media type to be set for the numbered ranking.
   */
  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }

  /**
   * Returns the numbered ranking dto as a string
   * @return the string representation of the numbered ranking dto
   */
  @Override
  public String toString() {
    return "NumberedRankingDTO{" +
      "id=" + id +
      ", userDTO=" + userDTO +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", isPublic=" + isPublic +
      ", isReverseOrder=" + isReverseOrder +
      ", mediaType='" + mediaType + '\'' +
      '}';
  }
}
