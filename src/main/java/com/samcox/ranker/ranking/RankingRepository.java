package com.samcox.ranker.ranking;

import com.samcox.ranker.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
  void deleteByUserId(Long userId);
  Optional<List<Ranking>> findByUser(User user);
}
