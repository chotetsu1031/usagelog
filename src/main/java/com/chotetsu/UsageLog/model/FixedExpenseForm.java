package com.chotetsu.UsageLog.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FixedExpenseForm {
  private String description;
  private Integer amount;
  private Long categoryCd;
  private String purchaseDate;
}