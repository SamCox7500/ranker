package com.samcox.ranker.ranking;

import com.samcox.ranker.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * Abstract class for a ranking created by a user.
 * Implementations contain the objects to be ranked.
 * This class contains details about the ranking such as the title,
 * desc, if the ranking is public or not, and the user that created it.
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Ranking {

  /**
   * Unique identifier for a ranking.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  /**
   * The ID of the user that owns the ranking.
   * Cannot be null.
   */
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  @NotNull(message = "Ranking must have a user assigned to it")
  private User user;

  /**
   * The title of the ranking. Must be between 1 and 30 chars.
   */
  @NotBlank(message = "Ranking must have a title")
  @Size(min = 1, max = 30, message = "Ranking title must be between 1 and 30 chars")
  private String title;

  /**
   * Description of the ranking. Must be between 1 and 150 chars.
   */
  @NotBlank(message = "Ranking must have a description")
  @Size(min = 1, max = 150, message = "Ranking description must be between 1 and 150 chars")
  private String description;

  /**
   * Determines if only the owner can access the list or if any user can.
   */
  private boolean isPublic;

  //todo type?

  /**
   * Default constructor as required by JPA.
   */
  protected Ranking() {}

  /**
   * Constructor for a ranking. Sets the user, title, desc and publicity.
   * @param user the user who owns the ranking
   * @param title the title of the ranking
   * @param desc the description of the ranking
   * @param isPublic whether the ranking can be accessed by all user or just the owner
   */
  protected Ranking(User user, String title, String desc, boolean isPublic) {
    this.user = user;
    this.title = title;
    this.description = desc;
    this.isPublic = isPublic;
  }

  /**
   * Returns the ID of the ranking.
   * @return
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the owner of the ranking.
   * @return the user who owns the ranking
   */
  public User getUser() {
    return user;
  }

  /**
   * Sets an owner for the ranking.
   * @param user the user to own the ranking
   */
  public void setUser(User user) {
    this.user = user;
  }

  /**
   * Returns the title for the ranking.
   * @return the title of the ranking
   */
  public String getTitle() {
    return title;
  }

  /**
   * Sets the title of the ranking.
   * @param title the new title for the ranking
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Returns the description for the ranking
   * @return the description for the ranking
   */
  public String getDescription() {
    return description;
  }

  /**
   * Sets the description for the ranking
   * @param description the description for the ranking
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Returns if the ranking is accessible to just the owner or any user
   * @return {@code true} if the ranking is open to all users, {@code false} if only the owner can access it
   */
  public boolean getIsPublic() {
    return isPublic;
  }

  /**
   * Sets the ranking to public. Making it open to all users.
   */
  public void setPublic() {
    isPublic = true;
  }

  /**
   * Closes the ranking to allow only the owner to access it.
   */
  public void setPrivate() {
    isPublic = false;
  }

  /**
   * Returns the ranking as a string.
   * @return the string representation of the ranking
   */
  @Override
  public String toString() {
    return "Ranking{" +
      "id=" + id +
      ", user=" + user +
      ", title='" + title + '\'' +
      ", description='" + description + '\'' +
      ", isPublic=" + isPublic +
      '}';
  }
}
