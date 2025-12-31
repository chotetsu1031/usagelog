package com.chotetsu.UsageLog.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "keyword")
@Getter
@Setter
public class Keyword {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)

  // カテゴリID
  private Long categoryId;

  // カテゴリコード
  private Long categoryCd;

  // カテゴリ名
  private String categoryName;

  // 分類キーワード
  private String keyWord;

  // 有効／無効（論理削除）
  private Long validateFlag;

}
