package com.example.ddd.presentation.controller;

import com.example.ddd.application.dto.CreateProductRequest;
import com.example.ddd.application.dto.ProductResponse;
import com.example.ddd.application.dto.UpdateProductRequest;
import com.example.ddd.application.usecase.CreateProductUseCase;
import com.example.ddd.application.usecase.GetProductUseCase;
import com.example.ddd.application.usecase.ListProductsUseCase;
import com.example.ddd.application.usecase.UpdateProductUseCase;
import com.example.ddd.presentation.dto.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Product operations.
 * This is the presentation layer entry point.
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@Tag(name = "Products", description = "Product management API - handles product catalog operations")
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final ListProductsUseCase listProductsUseCase;

    @Operation(
        summary = "Create a new product",
        description = "Creates a new product in the catalog with the provided details. Returns the created product with a generated ID."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Product successfully created",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data or product with same name already exists",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Product details to create",
                required = true
            )
            CreateProductRequest request) {
        log.info("Received request to create product: {}", request.name());
        ProductResponse response = createProductUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(
        summary = "Get product by ID",
        description = "Retrieves a single product by its unique identifier"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product found and returned",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found with the given ID",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(
            @Parameter(description = "Product ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String id) {
        log.debug("Received request to get product: {}", id);
        ProductResponse response = getProductUseCase.execute(id);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Update product information",
        description = "Updates the name and description of an existing product. Price and stock updates are handled separately."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Product successfully updated",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Product not found with the given ID",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid input data",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @Parameter(description = "Product ID", required = true, example = "550e8400-e29b-41d4-a716-446655440000")
            @PathVariable String id,
            @Valid @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                description = "Updated product information",
                required = true
            )
            UpdateProductRequest request) {
        log.info("Received request to update product: {}", id);
        ProductResponse response = updateProductUseCase.execute(id, request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "List all active products",
        description = "Retrieves a list of all active products in the catalog. Inactive and discontinued products are excluded."
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "List of products retrieved successfully",
            content = @Content(schema = @Schema(implementation = ProductResponse.class))
        )
    })
    @GetMapping
    public ResponseEntity<List<ProductResponse>> listProducts() {
        log.debug("Received request to list all products");
        List<ProductResponse> responses = listProductsUseCase.execute();
        return ResponseEntity.ok(responses);
    }
}
