package com.samcox.ranker.ranking;

import org.springframework.data.jpa.repository.JpaRepository;

public interface NumberedRankingRepository extends JpaRepository<NumberedRanking, Long> {}
