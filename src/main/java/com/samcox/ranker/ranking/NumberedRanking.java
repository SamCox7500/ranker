package com.samcox.ranker.ranking;

import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.user.User;
import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

@Entity
public class NumberedRanking extends Ranking {
  private boolean isReverseOrder;
  @OneToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "media_list_id", referencedColumnName = "id")
  private MediaList<?> mediaList;

  public NumberedRanking() {}
  public NumberedRanking(User user, String title, String desc, boolean isPublic, boolean isReverseOrder) {
    super(user, title, desc, isPublic);
    this.isReverseOrder = isReverseOrder;
  }

  public boolean getIsReverseOrder() {
    return isReverseOrder;
  }
  public void setReverseOrder(boolean isReverseOrder) {
    this.isReverseOrder = isReverseOrder;
  }

  public MediaList<?> getMediaList() {
    return mediaList;
  }

  public void setMediaList(MediaList<?> mediaList) {
    this.mediaList = mediaList;
  }
}
