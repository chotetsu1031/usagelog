package com.chotetsu.UsageLog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chotetsu.UsageLog.entity.Keyword;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
  Optional<Keyword> findByCategoryName(String categoryName);

  @Query("""
          SELECT k
          FROM Keyword k
          WHERE k.validateFlag = 1
      """)
  List<Keyword> findAllActive();
}
