package com.example.ddd.presentation.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Validation error response DTO.
 */
public record ValidationErrorResponse(
    int status,
    String error,
    Map<String, String> validationErrors,
    Instant timestamp
) {
}
