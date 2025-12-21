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
    // カテゴリマスタから有効なカテゴリを取得する
    List<Category> categories = categoryRepository.findAllActive();

    for (Category c : categories) {
      // 利用明細の内容がカテゴリマスタのキーワードに含まれる場合はそのカテゴリを返す
      if (description != null && description.contains(c.getKeyWord())) {
        return c;
      }
    }
    // カテゴリ「未分類」のデータを返す
    return categoryRepository.findByCategoryName("未分類")
        // カテゴリ「未分類」のデータが存在しなければエラー
        .orElseThrow(() -> new IllegalStateException("未分類カテゴリが未定義"));
  }
}
