package com.dev.discusso.common.exception;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ExceptionResponse {
  private String code;
  private int status;
  private String message;
  private LocalDateTime timestamp;

  public ExceptionResponse(String code, int status, String message) {
    this.code = code;
    this.status = status;
    this.message = message;
    this.timestamp = LocalDateTime.now();
  }

  public ExceptionResponse(String code) {
    this.code = code;
    this.timestamp = LocalDateTime.now();
  }
}
