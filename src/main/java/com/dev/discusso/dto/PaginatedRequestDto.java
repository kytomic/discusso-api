package com.dev.discusso.dto;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
public class PaginatedRequestDto {
  private int page;
  private int pageSize;
}
