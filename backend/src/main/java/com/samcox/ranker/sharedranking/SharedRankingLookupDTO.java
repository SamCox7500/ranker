package com.samcox.ranker.sharedranking;

/**
 * DTO for response when looking up shared ranking type.
 */
public class SharedRankingLookupDTO {

  /**
   * The type of ranking e.g. NUMBERED or TIER_LIST.
   */
  private String rankingType;
  /**
   * Token used to gain guest access to shared ranking.
   */
  private String shareToken;

  /**
   * Constructor for shared ranking lookup DTO.
   * @param rankingType the type of the shared ranking
   * @param shareToken the unique string used to identify the shared ranking
   */
  public SharedRankingLookupDTO(String rankingType, String shareToken) {
    this.rankingType = rankingType;
    this.shareToken = shareToken;
  }

  /**
   * Returns the ranking type of the shared ranking.
   * @return the ranking type of the shared ranking
   */
  public String getRankingType() {
    return rankingType;
  }

  /**
   * Sets the ranking type of DTO.
   * @param rankingType the type of the shared ranking
   */
  public void setRankingType(String rankingType) {
    this.rankingType = rankingType;
  }

  /**
   * Returns the share token.
   * @return the unique string used to identify a shared ranking
   */
  public String getShareToken() {
    return shareToken;
  }

  /**
   * Sets the share token for the DTO.
   * @param shareToken unique string for identifying shared rankings
   */
  public void setShareToken(String shareToken) {
    this.shareToken = shareToken;
  }
}
