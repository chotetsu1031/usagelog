package com.chotetsu.UsageLog.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.chotetsu.UsageLog.entity.Category;
import com.chotetsu.UsageLog.repository.CategoryRepository;

@Service
public class CategoryResolver {
  @Autowired
  private CategoryRepository categoryRepository;

  public Category resolve(String description) {
    List<Category> categories = categoryRepository.findAllActive();

    for (Category c : categories) {
      if (description != null && description.contains(c.getKeyWord())) {
        return c;
      }
    }
    return categoryRepository.findByCategoryName("未分類")
        .orElseThrow(() -> new IllegalStateException("未分類カテゴリが未定義"));
  }
}
