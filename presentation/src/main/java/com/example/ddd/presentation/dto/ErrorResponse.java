package com.example.ddd.presentation.dto;

import java.time.Instant;

/**
 * Standard error response DTO.
 */
public record ErrorResponse(
    int status,
    String error,
    String message,
    Instant timestamp
) {
}
