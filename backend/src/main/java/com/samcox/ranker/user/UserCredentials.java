package com.samcox.ranker.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Represents the user credentials used for authentication.
 * Also used for account creation.
 * Includes username and password.
 */
public class UserCredentials {

  /**
   * The username of the user
   * Must not be blank and at most 30 characters
   */
  @NotBlank(message = "User must have username")
  @Size(max = 30, message = "Max size of username is 30 chars")
  private String username;

  /**
   * The password of the user.
   * Plain text. Will be encrypted before being stored in the database.
   * Must not be blank. Must be between 8 and 64 characters.
   * Must contain at least one uppercase letter, one lowercase letter, one number and one special character.
   */
  @NotBlank(message = "User must have password")
  @Size(min = 8, max = 64, message = "Password must be between 8 and 64 characters")
  @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
    message = "Password must contain at least one uppercase letter, one lowercase letter," +
    " one number and one special character")
  private String password;

  /**
   * Returns the username of the user credentials.
   * @return the username in the user credentials
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the password of the user credentials.
   * @return the password of the user credentials
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets a new username for the credentials.
   * @param username the username of the credentials
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Sets a new password for the credentials.
   * @param password the password of the credentials
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Returns the user credentials as a string.
   * @return the string representation of the user credentials.
   */
  @Override
  public String toString() {
    return "UserCredentials{" +
      "username='" + username + '\'' +
      ", password='" + password + '\'' +
      '}';
  }

  /**
   * Returns if this set of user credentials is equal to the user credentials parameter
   * @param o the user credentials to be compared to this one
   * @return {@code true} if the objects have the same username and password. {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserCredentials that = (UserCredentials) o;
    return Objects.equals(username, that.username) && Objects.equals(password, that.password);
  }

  /**
   * Returns the hashcode of this user credentials object to be used for comparison.
   * @return the hashcode representation of this user credentials
   */
  @Override
  public int hashCode() {
    return Objects.hash(username, password);
  }
}
