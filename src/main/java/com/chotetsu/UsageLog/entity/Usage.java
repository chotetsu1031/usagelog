package com.chotetsu.UsageLog.entity;

import java.time.LocalDateTime;
import java.util.Date;
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
  private UUID usage_id;
  private String description;
  private Integer amount;
  private String category_id;
  private String note;
  private Integer validate_flag;
  private Date purchase_date;
  private Integer type;
  private String created_user_id;
  private String updated_user_id;
  private LocalDateTime created_date;
  private LocalDateTime updated_date;
  // getter/setter
}
