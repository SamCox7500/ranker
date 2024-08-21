package com.samcox.ranker.ranking;

import com.samcox.ranker.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Ranking {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @NotBlank(message = "Ranking must have a user assigned to it")
  private User user;

  @NotBlank(message = "Ranking must have a title")
  @Size(min = 1, max = 30, message = "Ranking title must be between 1 and 30 chars")
  private String title;

  @NotBlank(message = "Ranking must have a description")
  @Size(min = 1, max = 150, message = "Ranking description must be between 1 and 150 chars")
  private String description;

  private boolean isPublic;

  //todo type?

  protected Ranking() {}
  protected Ranking(User user, String title, String desc, boolean isPublic) {
    this.user = user;
    this.title = title;
    this.description = desc;
    this.isPublic = isPublic;
  }

  public Long getId() {
    return id;
  }

  public User getUser() {
    return user;
  }
  public void setUser(User user) {
    this.user = user;
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
  public boolean getIsPublic() {
    return isPublic;
  }
  public void setPublic() {
    isPublic = true;
  }
  public void setPrivate() {
    isPublic = false;
  }
}
