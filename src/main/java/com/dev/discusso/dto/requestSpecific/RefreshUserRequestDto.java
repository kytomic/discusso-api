package com.dev.discusso.dto.requestSpecific;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RefreshUserRequestDto {
  private String refreshToken;
}
