package com.samcox.ranker.ranking;

import com.samcox.ranker.user.UserDTO;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NumberedRankingDTO {
  private long id;
  @NotBlank(message = "NumberedRankingDTO must have a user assigned to it")
  private UserDTO userDTO;
  @NotBlank(message = "Ranking must have a title")
  @Size(min = 1, max = 30, message = "Ranking title must be between 1 and 30 chars")
  private String title;

  @NotBlank(message = "Ranking must have a description")
  @Size(min = 1, max = 150, message = "Ranking description must be between 1 and 150 chars")
  private String description;

  private boolean isPublic;
  private boolean isReverseOrder;

  public long getId() {
    return id;
  }
  public void setId(long id) {
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
}
