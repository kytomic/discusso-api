package com.dev.discusso.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseDto {
  private LocalDateTime createdAt;

  private String createdBy;

  private LocalDateTime updatedAt;

  private String updatedBy;

  @JsonProperty("isDeleted")
  private boolean isDeleted;

  public BaseDto(String createdBy, String updatedBy) {
    this.createdAt = LocalDateTime.now();
    this.createdBy = createdBy;
    this.updatedAt = LocalDateTime.now();
    this.updatedBy = updatedBy;
    this.isDeleted = false;
  }
}
