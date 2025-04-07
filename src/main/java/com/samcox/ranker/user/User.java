package com.samcox.ranker.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.Objects;

/**
 * Represents a user entity in the system.
 *
 * <p>This entity is mapped to the "user" table in the database. A user has a username, password and role.
 * All attributes have validation constraints.</p>
 *
 * @author Samuel Cox
 */
@Entity
@Table(name = "\"user\"")
public class User {

  /**
   * The unique identifier for the user.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /**
   * Username identifier for user.
   * Must not be blank and at most 30 characters
   */
  @NotBlank(message = "User must have a username")
  @Size(max = 30, message = "Max length of username is 30 characters")
  @Column(unique = true, nullable = false)
  private String username;

  /**
   * The user's encrypted password.
   * Must not be blank and at least 8 characters.
   */
  @NotBlank(message = "User must have a password")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;

  /**
   * The role of the user (e.g. "USER", "ADMIN").
   * Must not be blank.
   */
  @NotBlank(message = "User must have a role")
  private String role;

  /**
   * Default constructor required by jpa.
   * Initializes all fields to null.
   */
  public User(){
    this.username = null;
    this.password = null;
    this.role = null;
  }

  /**
   * Constructs a new {@code User} with the specified username, password and role.
   * @param username Username identifier for user.
   * @param password Encrypted password for user.
   * @param role Role of the user.
   */
  public User(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  /**
   * Returns the user's ID.
   * @return User ID
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the username of the user.
   * @return The user's username
   */
  public String getUsername() {
    return username;
  }

  /**
   * Returns the encrypted password of the user.
   * @return The user's encrypted password
   */
  public String getPassword() {
    return password;
  }

  /**
   * Sets the user's username.
   * @param username The new username
   */
  public void setUsername(String username) {
    this.username = username;
  }

  /**
   * Sets the user's password. Should be encrypted first.
   * @param password The user's new password
   */
  public void setPassword(String password) {
    this.password = password;
  }

  /**
   * Sets the role of the user
   * @param role The user's new role in the system.
   */
  public void setRole(String role) {
    this.role = role;
  }

  /**
   * Returns the {@code User} as a string.
   * @return String representation of the user
   */
  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      ", password='" + password + '\'' +
      ", role='" + role + '\'' +
      '}';
  }

  /**
   * Returns if another abject is equal to this one.
   * Equality is based on the ID, username, and role.
   *
   *
   * @param o the reference object with which to compare
   * @return {@code true} if this object has the same id, username, password, role. {@code false} otherwise
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    User user = (User) o;
    return id == user.id && Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(role, user.role);
  }

  /**
   * Returns the hash code value for this user.
   * Based on id, username, password, role.
   *
   * @return Hash code value for this user
   */
  @Override
  public int hashCode() {
    return Objects.hash(id, username, password, role);
  }
}
