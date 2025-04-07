package com.samcox.ranker.user;

import java.util.Objects;

/**
 * DTO for {@link User}
 * <p>Excludes password of the user for security reasons as this object is used for communication</p>
 *
 * @see User
 */
public class UserDTO {

  /**
   * The id of the user.
   */
  private Long id;

  /**
   * The username of the user.
   */
  private String username;

  /**
   * Default constructor as required by JPA.
   */
  public UserDTO() {}

  /**
   * Constructor for creating UserDTO with specified ID and username.
   * @param id the id of the user
   * @param username the username of the user
   */
  public UserDTO(Long id,String username) {
    this.id = id;
    this.username = username;
  }

  /**
   * Returns the id of the user
   * @return the id of the user
   */
  public Long getId() {
    return id;
  }

  /**
   * Sets a new id for the UserDTO
   * @param id the new id for the UserDTO
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Returns the username of the user
   * @return the username of the user
   */
  public String getUsername() {
    return username;
  }

  /**
   * Sets a new username for the UserDTO
   * @param username the new username for the UserDTO
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Returns if this UserDTO object is equal to the object parameter
   * @param o the object to be compared to this one
   * @return {@code true} if the UserDTO object has the same id and username as this one. {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserDTO userDTO = (UserDTO) o;
    return Objects.equals(id, userDTO.id) && Objects.equals(username, userDTO.username);
  }

  /**
   * Returns the hashcode for the UserDT0. Derived from the id and username.
   * @return hashcode representation of the UserDTO
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, username);
  }

  /**
   * Returns the string representation of the UserDTO
   * @return the string representation of the UserDTO
   */
  @Override
  public String toString() {
    return "UserDTO{" +
      "id=" + id +
      ", username='" + username + '\'' +
      '}';
  }
}
