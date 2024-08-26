package com.samcox.ranker.ranking;

import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.user.User;
import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

@Entity
public class NumberedRanking extends Ranking {
  private boolean isReverseOrder;
  @OneToOne
  @JoinColumn(name = "media_list_id")
  private MediaList mediaList;

  public NumberedRanking() {}
  public NumberedRanking(User user, String title, String desc, boolean isPublic, boolean isReverseOrder) {
    super(user, title, desc, isPublic);
    this.isReverseOrder = isReverseOrder;
    this.mediaList
  }

  public boolean getIsReverseOrder() {
    return isReverseOrder;
  }
  public void setReverseOrder(boolean isReverseOrder) {
    this.isReverseOrder = isReverseOrder;
  }
}
