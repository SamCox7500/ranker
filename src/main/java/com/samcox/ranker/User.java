package com.samcox.ranker;

import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private String username;
  private String password;
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
