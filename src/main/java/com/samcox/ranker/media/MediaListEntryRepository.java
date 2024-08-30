package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaListEntryRepository extends JpaRepository<MediaListEntry, Long> {
  Optional<MediaListEntry> findByMediaListAndRanking(MediaList mediaList, int ranking);
  Optional<List<MediaListEntry>> findByMediaList(MediaList mediaList);
}
