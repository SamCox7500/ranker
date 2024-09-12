package com.samcox.ranker.ranking;

import com.samcox.ranker.media.MediaList;
import com.samcox.ranker.media.MediaType;
import com.samcox.ranker.user.User;
import jakarta.persistence.*;
import org.springframework.validation.annotation.Validated;

import javax.print.attribute.standard.Media;

@Entity
public class NumberedRanking extends Ranking {
  private boolean isReverseOrder;
  @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
  @JoinColumn(name = "media_list_id", referencedColumnName = "id")
  private MediaList mediaList;

  @Enumerated(EnumType.STRING)
  private MediaType mediaType;

  public NumberedRanking() {}


  public NumberedRanking(User user, String title, String desc, boolean isPublic, boolean isReverseOrder, MediaType mediaType, MediaList mediaList) {
    super(user, title, desc, isPublic);
    this.isReverseOrder = isReverseOrder;
    this.mediaType = mediaType;
    this.mediaList = mediaList;
    mediaList.setNumberedRanking(this);
  }
  public boolean getIsReverseOrder() {
    return isReverseOrder;
  }
  public void setReverseOrder(boolean isReverseOrder) {
    this.isReverseOrder = isReverseOrder;
  }

  public MediaList getMediaList() {
    return mediaList;
  }
  public void setMediaList(MediaList mediaList) {
    this.mediaList = mediaList;
    mediaList.setNumberedRanking(this);
  }

  public MediaType getMediaType() {
    return mediaType;
  }
  public void setMediaType(MediaType mediaType) {
    this.mediaType = mediaType;
  }
}
