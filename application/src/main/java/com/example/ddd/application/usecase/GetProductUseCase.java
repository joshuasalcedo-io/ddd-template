package com.example.ddd.application.usecase;

import com.example.ddd.application.dto.ProductResponse;
import com.example.ddd.application.mapper.ProductMapper;
import com.example.ddd.domain.exception.EntityNotFoundException;
import com.example.ddd.domain.aggregateroot.product.Product;
import com.example.ddd.domain.aggregateroot.product.ProductId;
import com.example.ddd.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Use case for retrieving a product by ID.
 */
@Slf4j
@RequiredArgsConstructor
public class GetProductUseCase {

    private final ProductRepository productRepository;

    public ProductResponse execute(String productId) {
        log.debug("Fetching product with ID: {}", productId);

        Product product = productRepository.findById(ProductId.of(productId))
            .orElseThrow(() -> EntityNotFoundException.forId(Product.class, productId));

        return ProductMapper.toResponse(product);
    }
}
