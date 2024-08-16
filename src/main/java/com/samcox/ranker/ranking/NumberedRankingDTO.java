package com.samcox.ranker.ranking;

import com.samcox.ranker.user.UserDTO;

public class NumberedRankingDTO {
  private long id;
  private UserDTO userDTO;
  private String title;
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
