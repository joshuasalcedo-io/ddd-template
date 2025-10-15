package com.example.ddd.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * DTO for product responses.
 */
@Schema(description = "Product information")
public record ProductResponse(
    @Schema(description = "Unique product identifier", example = "550e8400-e29b-41d4-a716-446655440000")
    String id,

    @Schema(description = "Product name", example = "Laptop")
    String name,

    @Schema(description = "Product description", example = "High-performance laptop for developers")
    String description,

    @Schema(description = "Product price", example = "999.99")
    BigDecimal price,

    @Schema(description = "Currency code (ISO 4217)", example = "USD")
    String currency,

    @Schema(description = "Available stock quantity", example = "10")
    int stockQuantity,

    @Schema(description = "Product status", example = "ACTIVE", allowableValues = {"ACTIVE", "INACTIVE", "DISCONTINUED"})
    String status,

    @Schema(description = "Product creation timestamp", example = "2025-10-15T10:30:00Z")
    Instant createdAt,

    @Schema(description = "Last update timestamp", example = "2025-10-15T14:45:00Z")
    Instant updatedAt
) {
}
