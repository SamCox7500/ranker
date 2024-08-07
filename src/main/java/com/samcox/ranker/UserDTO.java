package com.samcox.ranker;

import jakarta.validation.constraints.NotNull;

import java.util.Objects;

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
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDTO userDTO = (UserDTO) o;
    return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username);
  }
  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }
}
