package com.chotetsu.UsageLog.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchListForm {
  private String description;
  private Long categoryCd;
  private String purchaseMonth;

  // 空文字列をnullに変換するセッター
  public void setPurchaseMonth(String purchaseMonth) {
    this.purchaseMonth = (purchaseMonth == null || purchaseMonth.isEmpty()) ? null : purchaseMonth;
  }

  public void setCategoryCd(Long categoryCd) {
    this.categoryCd = (categoryCd == null || categoryCd == 0L) ? null : categoryCd;
  }
}
