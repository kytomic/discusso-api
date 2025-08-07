package com.dev.discusso.common.exception;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
  private final ObjectMapper objectMapper;

  public GlobalExceptionHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ExceptionResponse> handleException(CustomException err) {
    ExceptionResponse exceptionResponse = getErrorResponse(err.getCode());
    exceptionResponse.setTimestamp(LocalDateTime.now());

    HttpStatus status = switch (exceptionResponse.getStatus()) {
      case 400 -> HttpStatus.BAD_REQUEST;
      case 401 -> HttpStatus.UNAUTHORIZED;
      case 403 -> HttpStatus.FORBIDDEN;
      case 404 -> HttpStatus.NOT_FOUND;
      default -> HttpStatus.INTERNAL_SERVER_ERROR;
    };

    return new ResponseEntity<ExceptionResponse>(exceptionResponse, status);
  }

  public ExceptionResponse getErrorResponse(String code) {
    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("exceptions.json")) {
      if (inputStream == null) {
        throw new IOException("File not found: exceptions.json");
      }

      JsonNode jsonNode = objectMapper.readTree(inputStream);
      JsonNode errorNode = jsonNode.get(code);
      if (errorNode != null) {
        return objectMapper.treeToValue(errorNode, ExceptionResponse.class);
      } else {
        throw new RuntimeException("Unknown error code: " + code);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
