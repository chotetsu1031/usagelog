package com.chotetsu.UsageLog.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.chotetsu.UsageLog.entity.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Optional<Category> findByCategoryName(String categoryName);

  @Query("""
          SELECT c
          FROM Category c
          WHERE c.validateFlag = 1
      """)
  List<Category> findAllActive();
}
