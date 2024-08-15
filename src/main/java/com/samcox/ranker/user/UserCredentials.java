package com.samcox.ranker.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

public class UserCredentials {
  @NotBlank(message = "User must have username")
  @Size(max = 30, message = "Max size of username is 30 chars")
  private String username;
  @NotBlank(message = "User must have password")
  @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "Password must contain at least one uppercase letter, one lowercase letter," +
    " one number and one special character")
  private String password;

  public String getUsername() {
    return username;
  }
  public String getPassword() {
    return password;
  }
  public void setUsername(String username) {
    this.username = username;
  }
  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "UserCredentials{" +
      "username='" + username + '\'' +
      ", password='" + password + '\'' +
      '}';
  }
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserCredentials that = (UserCredentials) o;
    return Objects.equals(username, that.username) && Objects.equals(password, that.password);
  }
  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }
}
