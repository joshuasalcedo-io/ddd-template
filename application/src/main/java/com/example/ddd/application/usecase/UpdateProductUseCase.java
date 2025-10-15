package com.example.ddd.application.usecase;

import com.example.ddd.application.dto.ProductResponse;
import com.example.ddd.application.dto.UpdateProductRequest;
import com.example.ddd.application.mapper.ProductMapper;
import com.example.ddd.domain.event.EventPublisher;
import com.example.ddd.domain.exception.EntityNotFoundException;
import com.example.ddd.domain.aggregateroot.product.Product;
import com.example.ddd.domain.aggregateroot.product.ProductId;
import com.example.ddd.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use case for updating product information.
 */
@Slf4j
@RequiredArgsConstructor
public class UpdateProductUseCase {

    private final ProductRepository productRepository;
    private final EventPublisher eventPublisher;

    public ProductResponse execute(String productId, UpdateProductRequest request) {
        log.info("Updating product with ID: {}", productId);

        Product product = productRepository.findById(ProductId.of(productId))
            .orElseThrow(() -> EntityNotFoundException.forId(Product.class, productId));

        // Update product (domain logic)
        product.updateInfo(request.name(), request.description());

        // Save product
        Product updatedProduct = productRepository.save(product);

        // Publish domain events
        updatedProduct.getDomainEvents().forEach(eventPublisher::publish);

        log.info("Product updated: {}", productId);

        return ProductMapper.toResponse(updatedProduct);
    }
}
