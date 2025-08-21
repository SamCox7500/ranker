package com.samcox.ranker.numberedranking;

import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.media.MediaType;
import com.samcox.ranker.ranking.Ranking;
import com.samcox.ranker.ranking.RankingType;
import com.samcox.ranker.user.User;
import jakarta.persistence.*;

/**
 * Represents a numbered ranking entity in the system.
 * <p>An implementation of the abstract {@link Ranking} class.
 * A type of ranking where each ranked media appears one after the other in a list,
 * with a number representing the user's rank for that media.
 * Can contain different media types.
 * Can be in ascending or descending order.</p>
 *
 * @see Ranking
 * @see MediaList
 * @see MediaType
 */
@Entity
public class NumberedRanking extends Ranking {
  /**
   * Reverse order means the media items will listed in descending order.
   * The last item will be the number one ranked item.
   * Otherwise, the list will be in ascending order. The first item will be the number one ranked item.
   */
  private boolean isReverseOrder;
  /**
   * The ranked list of media entries attached to the numbered ranking. See {@link MediaList}.
   * If the numbered ranking is deleted, the media list is also deleted.
   */
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "media_list_id", referencedColumnName = "id")
  private MediaList mediaList;

  /**
   * Default constructor required by JPA.
   */
  public NumberedRanking() {}

  /**
   * Creates a new numbered ranking.
   * @param user the user who is creating the numbered ranking
   * @param title the title of the numbered ranking
   * @param desc the description of the numbered ranking
   * @param isPublic if the numbered ranking can be accessed by just its creator or any user. Not yet implemented
   * @param isReverseOrder if the numbered ranking is in reverse order - highest ranked entry last
   * @param mediaType the type of media the numbered ranking contains
   * @param mediaList the media list of ranked entries belonging to the numbered ranking
   */
  public NumberedRanking(User user, String title, String desc, boolean isPublic, RankingType rankingType, MediaType mediaType, boolean isReverseOrder, MediaList mediaList) {
    super(user, title, desc, isPublic, rankingType, mediaType);
    this.isReverseOrder = isReverseOrder;
    this.mediaList = mediaList;
  }

  /**
   * Returns if the media list will have the highest ranked item last.
   * @return {@code true} if the highest ranked item is last. {@code false} otherwise
   */
  public boolean getIsReverseOrder() {
    return isReverseOrder;
  }

  /**
   * Sets if the numbered ranking will be in reverse order
   * @param isReverseOrder if the numbered ranking will be in reverse order or not
   */
  public void setReverseOrder(boolean isReverseOrder) {
    this.isReverseOrder = isReverseOrder;
  }

  /**
   * Returns the media list for the numbered ranking
   * @return the media list for the numbered ranking
   */
  public MediaList getMediaList() {
    return mediaList;
  }

  /**
   * Sets the media list for the numbered ranking
   * @param mediaList the {@link MediaList of the numbered ranking}
   */
  public void setMediaList(MediaList mediaList) {
    this.mediaList = mediaList;
  }
}
