package com.dev.discusso.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class PaginationResponseDto {
  private int page;
  private int pageSize;
  private int totalCount;
}
