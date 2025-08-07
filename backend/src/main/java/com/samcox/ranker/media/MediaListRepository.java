package com.samcox.ranker.media;

import com.samcox.ranker.ranking.NumberedRanking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.attribute.standard.Media;
import java.util.Optional;

/**
 * Repository interface for performing CRUD operations on {@link MediaList} entities.
 *  <p>This interface extends {@link JpaRepository} to provide standard methods like
 *  {@code save}, {@code findById}, {@code delete}, and more.
 *  It also includes a custom method for finding a {@code MediaList} by the {@link NumberedRanking} attached to it.
 * @see MediaList
 * @see NumberedRanking
 */
@Repository
public interface MediaListRepository extends JpaRepository<MediaList, Long> {
  //Optional<MediaList> findByNumberedRanking(NumberedRanking numberedRanking);
}
