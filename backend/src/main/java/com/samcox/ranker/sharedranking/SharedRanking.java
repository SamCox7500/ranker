package com.samcox.ranker.sharedranking;

import com.samcox.ranker.ranking.Ranking;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

/**
 * Class representing a ranking that has been shared by the ranking's owner.
 * The shared ranking is accessible to users that are not the owner by the unique share token.
 */
@Entity
public class SharedRanking {

  /**
   * Unique identifier for the shared ranking.
   */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  /**
   * Identifier for the shared ranking that is used in a link for other users (users that are not the owner) to access ranking.
   */
  @NotBlank(message = "Shared ranking must have a share token.")
  private String shareToken;

  /**
   * The ranking that is being shared by the user that owns it.
   */
  @OneToOne
  @JoinColumn(name = "ranking_id", nullable = false, unique = true)
  private Ranking ranking;

  /**
   * Default constructor as required by JPA.
   */
  public SharedRanking() {}

  /**
   * Constructor for the shared ranking.
   * Sets the share token used to access it and the ranking to be made accessible by that token.
   * @param shareToken unique string used by non-owner users to access the shared ranking
   * @param ranking the ranking being shared by its owner
   */
  public SharedRanking(String shareToken, Ranking ranking) {
    this.shareToken = shareToken;
    this.ranking = ranking;
  }

  /**
   * Returns the id of the shared ranking.
   * @return unique identifier for the shared ranking
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the share token of the shared ranking.
   * @return the unique share token of the ranking.
   */
  public String getShareToken() {
    return shareToken;
  }

  /**
   * Sets the share token of the shared ranking.
   * @param shareToken the unique string identifier of the ranking
   */
  public void setShareToken(String shareToken) {
    this.shareToken = shareToken;
  }

  /**
   * Sets the ranking to be shared by its owner.
   * @param ranking the ranking to be shared
   */
  public void setRanking(Ranking ranking) {
    this.ranking = ranking;
  }

  /**
   * Returns the ranking shared by the shared ranking
   * @return the ranking being shared
   */
  public Ranking getRanking() {
    return ranking;
  }
}
