package com.chotetsu.UsageLog.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchListForm {
  private String description;
  private Long categoryCd;
  private String purchaseMonth;
}
