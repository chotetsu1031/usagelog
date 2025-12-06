package com.chotetsu.UsageLog.model;

public class CsvRecord {
  private String description;
  private int amount;
  private String purchase_date;

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

  public String getPurchase_date() {
    return purchase_date;
  }

  public void setPurchase_date(String purchase_date) {
    this.purchase_date = purchase_date;
  }
}
