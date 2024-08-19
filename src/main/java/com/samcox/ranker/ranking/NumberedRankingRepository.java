package com.samcox.ranker.ranking;

import com.samcox.ranker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NumberedRankingRepository extends JpaRepository<NumberedRanking, Long> {
  Optional<List<NumberedRanking>> findByUser(User user);
  Optional<NumberedRanking> findById(long id);

  Optional<NumberedRanking> findByIdAndUser(long id, User user);
  void deleteByUser(User user);
  void deleteByIdAndUser(long id, User user);
}
