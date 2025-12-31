package com.chotetsu.UsageLog.model;

public class CsvRecord {
  private String description;
  private int amount;
  private String purchaseDate;
  private int validateFlag;
  private String categoryTips;

  // getter / setter
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

  public String getPurchaseDate() {
    return purchaseDate;
  }

  public void setPurchaseDate(String purchaseDate) {
    this.purchaseDate = purchaseDate;
  }

  public int getValidate_flag() {
    return validateFlag;
  }

  public void setValidate_flag(int validateFlag) {
    this.validateFlag = validateFlag;
  }

  public String getCategoryTips() {
    return categoryTips;
  }

  public void setCategoryTips(String categoryTips) {
    this.categoryTips = categoryTips;
  }
}
