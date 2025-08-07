package com.dev.discusso.schema;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseModel {
  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @Column(name = "created_by")
  private String createdBy;

  @Column(name = "updated_at")
  private LocalDateTime updatedAt;

  @Column(name = "updated_by")
  private String updatedBy;

  @Column(name = "is_deleted")
  @JsonProperty("isDeleted")
  private boolean isDeleted;

  public BaseModel(String createdBy, String updatedBy) {
    this.createdAt = LocalDateTime.now();
    this.createdBy = createdBy;
    this.updatedAt = LocalDateTime.now();
    this.updatedBy = updatedBy;
    this.isDeleted = false;
  }
}
