package com.example.ddd.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * DTO for creating a new product.
 */
@Schema(description = "Request to create a new product in the catalog")
public record CreateProductRequest(
    @Schema(description = "Product name", example = "Laptop", required = true)
    @NotBlank(message = "Product name is required")
    String name,

    @Schema(description = "Product description", example = "High-performance laptop for developers")
    String description,

    @Schema(description = "Product price", example = "999.99", required = true)
    @NotNull(message = "Price is required")
    @Min(value = 0, message = "Price must be non-negative")
    BigDecimal price,

    @Schema(description = "Currency code (ISO 4217)", example = "USD", required = true)
    @NotBlank(message = "Currency is required")
    String currency,

    @Schema(description = "Initial stock quantity", example = "10", required = true)
    @NotNull(message = "Initial stock is required")
    @Min(value = 0, message = "Initial stock must be non-negative")
    Integer initialStock
) {
}
