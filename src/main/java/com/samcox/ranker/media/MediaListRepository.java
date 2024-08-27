package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MediaListRepository extends JpaRepository<MediaList, Long> {
  Optional<MediaList> findByNumberedRanking(NumberedRanking numberedRanking);
}
