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
  private final String CATEGORY_FOOD = "５％";
  private final String CATEGORY_CLOTHS_BEAUTY = "１０％";
  private final String CATEGORY_DAILY_USE = "８％";

  public Category resolve(String description, String categoryTips) {
    // カテゴリマスタから有効なカテゴリを取得する
    List<Category> categories = categoryRepository.findAllActive();

    for (Category c : categories) {
      // 利用明細の内容がカテゴリマスタのキーワードに含まれる場合はそのカテゴリを返す
      if (description != null && description.contains(c.getKeyWord())) {
        return c;
      }
    }
    if (categoryTips.contains(CATEGORY_FOOD)) {
      return categoryRepository.findByCategoryName("食費")
          .orElseThrow(() -> new IllegalStateException("食費カテゴリが未定義"));
    }
    if (categoryTips.contains(CATEGORY_CLOTHS_BEAUTY)) {
      return categoryRepository.findByCategoryName("衣服・美容")
          .orElseThrow(() -> new IllegalStateException("衣服・美容カテゴリが未定義"));
    }
    if (categoryTips.contains(CATEGORY_DAILY_USE)) {
      return categoryRepository.findByCategoryName("日用品")
          .orElseThrow(() -> new IllegalStateException("日用品カテゴリが未定義"));
    }
    // カテゴリ「未分類」のデータを返す
    return categoryRepository.findByCategoryName("未分類")
        // カテゴリ「未分類」のデータが存在しなければエラー
        .orElseThrow(() -> new IllegalStateException("未分類カテゴリが未定義"));
  }
}
