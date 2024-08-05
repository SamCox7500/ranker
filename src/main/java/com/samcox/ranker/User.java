package com.samcox.ranker;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "\"user\"")
public class User {

  public enum Role {
    USER,
    ADMIN
  }

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  @NotBlank(message = "User must have a username")
  @Size(max = 30, message = "Max length of username is 30 characters")
  @Column(unique = true, nullable = false)
  private String username;
  @NotBlank(message = "User must have a password")
  @Size(min = 8, message = "Password must be at least 8 characters")
  private String password;
  @NotBlank(message = "User must have a role")
  @Enumerated(EnumType.STRING)
  private String role;

  public User(){
    this.username = null;
    this.password = null;
    this.role = null;
  }
  public User(String username, String password, String role) {
    this.username = username;
    this.password = password;
    this.role = role;
  }

  public long getId() {
    return id;
  }
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
  public void setRole(String role) {
    this.role = role;
  }
  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      ", password='" + password + '\'' +
      ", role='" + role + '\'' +
      '}';
  }
}
