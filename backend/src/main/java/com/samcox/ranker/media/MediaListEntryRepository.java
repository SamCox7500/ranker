package com.samcox.ranker.media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link MediaListEntry} entities.
 *
 * <p>This interface extends {@link JpaRepository} to provide standard methods like
 *  * {@code save}, {@code findById}, {@code delete}, and more.
 *  It also includes custom method for finding {@code MediaListEntry} by various parameters.</p>
 *  <p>Spring Data JPA automatically generates the implementation at runtime</p>
 *
 * @see MediaListEntry
 * @see MediaList
 */
@Repository
public interface MediaListEntryRepository extends JpaRepository<MediaListEntry, Long> {
  /**
   * Finds the {@link MediaListEntry} by {@link MediaList} and ranking.
   * @param mediaList the MediaList to which the entry belong
   * @param ranking the ranking of the entry in the MediaList
   * @return Optional containing MediaListEntry. Empty if no entry exists for given MediaList and ranking
   */
  Optional<MediaListEntry> findByMediaListAndRanking(MediaList mediaList, int ranking);

  /**
   * Finds a list of {@link MediaListEntry} by {@link MediaList}.
   * @param mediaList the MediaList to which the entries belong
   * @return optional list of all MediaListEntries of the MediaList. Empty if MediaList has no entries.
   */
  Optional<List<MediaListEntry>> findByMediaList(MediaList mediaList);

  /**
   * Finds a {@link MediaListEntry} by {@link MediaList} and id.
   * @param mediaList the media list to which the entry belongs
   * @param id the id of the entry
   * @return optional MediaListEntry. Empty if one could not be found for that media list and id
   */
  Optional<MediaListEntry> findByMediaListAndId(MediaList mediaList, Long id);
}
