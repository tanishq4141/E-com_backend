package com.ECOM.BUYIT.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
    log.error("Runtime exception occurred: ", ex);

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", Instant.now().toString());
    errorResponse.put("error", "Bad Request");
    errorResponse.put("message", ex.getMessage());
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException ex) {
    log.error("Validation exception occurred: ", ex);

    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", Instant.now().toString());
    errorResponse.put("error", "Validation Failed");
    errorResponse.put("message", "Invalid request data");
    errorResponse.put("status", HttpStatus.BAD_REQUEST.value());
    errorResponse.put("validationErrors", errors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
    log.error("Unexpected exception occurred: ", ex);

    Map<String, Object> errorResponse = new HashMap<>();
    errorResponse.put("timestamp", Instant.now().toString());
    errorResponse.put("error", "Internal Server Error");
    errorResponse.put("message", "An unexpected error occurred");
    errorResponse.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }
}
