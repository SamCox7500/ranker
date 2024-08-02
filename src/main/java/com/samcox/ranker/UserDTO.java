package com.samcox.ranker;

public class UserDTO {
  private Long id;
  private String username;

  public UserDTO() {
    this.id = null;
    this.username = null;
  }
  public UserDTO(long id,String username) {
    this.id = id;
    this.username = username;
  }
  public long getId() {
    return id;
  }
  public void setId(long id) {
    this.id = id;
  }
  public String getUsername() {
    return username;
  }
  public void setUsername(String username) {
    this.username = username;
  }
}
