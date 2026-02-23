package com.chotetsu.UsageLog.model;

import java.util.List;
import java.util.UUID;

public class CategoryUpdateRequest {
  private List<UUID> usageIds;
  private Long categoryCd;

  public List<UUID> getUsageIds() {
    return usageIds;
  }

  public void setUsageIds(List<UUID> usageIds) {
    this.usageIds = usageIds;
  }

  public Long getCategoryCd() {
    return categoryCd;
  }

  public void setCategoryCd(Long categoryCd) {
    this.categoryCd = categoryCd;
  }
}
