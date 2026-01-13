package com.chotetsu.UsageLog.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "usage_log")
public class Usage {

  @Id
  @Column(columnDefinition = "BINARY(16)")
  private UUID usageId;
  private String description;
  private Integer amount;
  private Long categoryCd;
  private String categoryName;
  private String note;
  private Integer validateFlag;
  @Column(columnDefinition = "DATE")
  private LocalDate purchaseDate;
  private Integer type;
  private String createdUserId;
  private String updatedUserId;
  private LocalDateTime createdDate;
  private LocalDateTime updatedDate;
  // getter/setter
}
