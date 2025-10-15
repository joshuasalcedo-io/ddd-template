package com.example.ddd.application.usecase;

import com.example.ddd.application.dto.CreateProductRequest;
import com.example.ddd.application.dto.ProductResponse;
import com.example.ddd.application.mapper.ProductMapper;
import com.example.ddd.domain.event.EventPublisher;
import com.example.ddd.domain.exception.InvalidDomainStateException;
import com.example.ddd.domain.aggregateroot.product.Product;
import com.example.ddd.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use case for creating a new product.
 * Implements the application service pattern.
 */
@Slf4j
@RequiredArgsConstructor
public class CreateProductUseCase {

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public ProductResponse execute(CreateProductRequest request) {
        log.info("Creating product with name: {}", request.name());

        // Check if product with same name already exists
        if (productRepository.existsByName(request.name())) {
            throw new InvalidDomainStateException(
                "Product with name '" + request.name() + "' already exists"
            );
        }

        // Create product (domain logic)
        Product product = ProductMapper.toDomain(request);

        // Save product
        Product savedProduct = productRepository.save(product);

        // Publish domain events
        savedProduct.getDomainEvents().forEach(eventPublisher::publish);

        log.info("Product created with ID: {}", savedProduct.getId());

        return ProductMapper.toResponse(savedProduct);
    }
}
