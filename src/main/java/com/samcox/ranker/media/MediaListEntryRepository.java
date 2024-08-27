package com.samcox.ranker.media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaListEntryRepository extends JpaRepository<MediaListEntry, Long> {}
