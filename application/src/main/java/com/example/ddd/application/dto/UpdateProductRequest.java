package com.example.ddd.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * DTO for updating product information.
 */
@Schema(description = "Request to update product name and description")
public record UpdateProductRequest(
    @Schema(description = "Updated product name", example = "Updated Laptop", required = true)
    @NotBlank(message = "Product name is required")
    String name,

    @Schema(description = "Updated product description", example = "Updated high-performance laptop")
    String description
) {
}
