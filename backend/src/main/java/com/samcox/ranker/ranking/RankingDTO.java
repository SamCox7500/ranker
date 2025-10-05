package com.samcox.ranker.ranking;

import com.samcox.ranker.user.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * DTO for {@link Ranking}.
 *
 * @see Ranking
 * @see UserDTO
 * @see com.samcox.ranker.user.User
 */
public class RankingDTO {

  /**
   * Unique identifier for the ranking.
   */
  private Long id;

  /**
   * DTO for the {@link com.samcox.ranker.user.User} that owns the ranking. Must not be null.
   */
  @NotNull(message = "RankingDTO must have a user assigned to it")
  private UserDTO userDTO;

  /**
   * The title of the ranking. Must be between 1 and 30 characters.
   */
  @NotBlank(message = "Ranking must have a title")
  @Size(min = 1, max = 30, message = "Ranking title must be between 1 and 30 chars")
  private String title;

  /**
   * The description of the ranking. Must be between 1 and 150 characters.
   */
  @NotBlank(message = "Ranking must have a description")
  @Size(min = 1, max = 150, message = "Ranking description must be between 1 and 150 chars")
  private String description;

  /**
   * If the numbered ranking is open to all user or just the owner.
   */
  private boolean isPublic;

  /**
   * tHE
   */
  private String rankingType;

  private String mediaType;

  /**
   * Default constructor as required by JPA.
   */
  public RankingDTO() {}
  /**
   * Constructor for creating Ranking objects
   */
  public RankingDTO(Long id, UserDTO userDTO, String title, String desc, boolean isPublic, String rankingType, String mediaType) {
    this.id = id;
    this.userDTO = userDTO;
    this.title = title;
    this.description = desc;
    this.isPublic = isPublic;
    this.rankingType = rankingType;
    this.mediaType = mediaType;
  }

  /**
   * Returns the unique identifier of the ranking.
   * @return the id of the ranking
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets the unique identifier for the ranking
   * @param id the id of the ranking to be set
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the {@link UserDTO} of the user that owns the ranking.
   * @return the UserDTO of the user that owns the ranking
   */
  public UserDTO getUserDTO() {
    return userDTO;
  }
  /**
   * Sets the UserDTO for the ranking DTO.
   * @param userDTO the userDTO of the user to be set as the owner of the ranking
   */
  public void setUserDTO(UserDTO userDTO) {
    this.userDTO = userDTO;
  }
  /**
   * Returns the title of the ranking.
   * @return the title of the ranking
   */
  public String getTitle() {
    return title;
  }
  /**
   * Sets the title of the ranking.
   * @param title of the ranking to be set
   */
  public void setTitle(String title) {
    this.title = title;
  }
  /**
   * Returns the description of the ranking.
   * @return the description of the ranking
   */
  public String getDescription() {
    return description;
  }
  /**
   * Sets the description of the ranking.
   * @param desc the description of the ranking
   */
  public void setDescription(String desc) {
    this.description = desc;
  }
  /**
   * Returns if the ranking is public or not.
   * @return {@code true} if the ranking is public. {@code false} otherwise.
   */
  public boolean isPublic() {
    return isPublic;
  }
  /**
   * Sets if the ranking is public or not
   * @param isPublic {@code true} if the ranking is to be public, {@code false otherwise}.
   */
  public void setPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }

  /**
   * Returns ranking type. E.g. Numbered Ranking, Tier List.
   * @return the type of the ranking
   */
  public String getRankingType() {
    return rankingType;
  }

  /**
   * Sets the type of the ranking
   * @param rankingType the enum type of the ranking
   */
  public void setRankingType(String rankingType) {
    this.rankingType = rankingType;
  }

  /**
   * Returns the media type that the ranking contains. E.g. Movies, TV Shows.
   * @return the media type of the ranking
   */
  public String getMediaType() {
    return mediaType;
  }

  /**
   * Sets the media type of the ranking.
   * @param mediaType the media type of the ranking
   */
  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }
  /**
   * Returns the ranking dto as a string
   * @return the string representation of the ranking dto
   */
  @Override
  public String toString() {
    return "RankingDTO{" +
      "id=" + id +
      ", userDTO=" + userDTO +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", isPublic=" + isPublic +
      ", rankingType=" + rankingType +
      ", mediaType=" + mediaType +
      '}';
  }
}
