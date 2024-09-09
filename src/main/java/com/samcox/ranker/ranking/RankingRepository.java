package com.samcox.ranker.ranking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RankingRepository extends JpaRepository<Ranking, Long> {
  void deleteByUserId(Long userId);
}
