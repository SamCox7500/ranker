package com.samcox.ranker;

import jakarta.persistence.*;

@Entity
@Table(name = "\"user\"")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private long id;
  private final String username;
  private final String email;


  public User(){
    this.username = null;
    this.email = null;
  }
  public User(String username, String email) {
    this.username = username;
    this.email = email;
  }

  public long getId() {
    return id;
  }
  public String getUsername() {
    return username;
  }
  public String getEmail() {
    return email;
  }
  @Override
  public String toString() {
    return "User{" +
      "id=" + id +
      ", username='" + username + '\'' +
      ", email='" + email + '\'' +
      '}';
  }
}
