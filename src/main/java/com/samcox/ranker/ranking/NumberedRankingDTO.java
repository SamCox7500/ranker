package com.samcox.ranker.ranking;

import com.samcox.ranker.user.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.validation.annotation.Validated;

public class NumberedRankingDTO {
  private Long id;
  @NotNull(message = "NumberedRankingDTO must have a user assigned to it")
  private UserDTO userDTO;
  @NotBlank(message = "Ranking must have a title")
  @Size(min = 1, max = 30, message = "Ranking title must be between 1 and 30 chars")
  private String title;

  @NotBlank(message = "Ranking must have a description")
  @Size(min = 1, max = 150, message = "Ranking description must be between 1 and 150 chars")
  private String description;

  private boolean isPublic;
  private boolean isReverseOrder;

  private String mediaType;

  public NumberedRankingDTO() {}
  public NumberedRankingDTO(long id, UserDTO userDTO, String title, String desc, boolean isPublic, boolean isReverseOrder, String mediaType) {
    this.id = id;
    this.userDTO = userDTO;
    this.title = title;
    this.description = desc;
    this.isPublic = isPublic;
    this.isReverseOrder = isReverseOrder;
    this.mediaType = mediaType;
  }

  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
  public UserDTO getUserDTO() {
    return userDTO;
  }
  public void setUserDTO(UserDTO userDTO) {
    this.userDTO = userDTO;
  }
  public String getTitle() {
    return title;
  }
  public void setTitle(String title) {
    this.title = title;
  }
  public String getDescription() {
    return description;
  }
  public void setDescription(String description) {
    this.description = description;
  }
  public boolean isPublic() {
    return isPublic;
  }
  public void setPublic(boolean isPublic) {
    this.isPublic = isPublic;
  }
  public boolean isReverseOrder() {
    return isReverseOrder;
  }
  public void setReverseOrder(boolean reverseOrder) {
    isReverseOrder = reverseOrder;
  }
  public String getMediaType() {
    return mediaType;
  }
  public void setMediaType(String mediaType) {
    this.mediaType = mediaType;
  }
}
