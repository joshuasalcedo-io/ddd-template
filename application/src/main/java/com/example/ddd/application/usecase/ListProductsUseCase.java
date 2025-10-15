package com.example.ddd.application.usecase;

import com.example.ddd.application.dto.ProductResponse;
import com.example.ddd.application.mapper.ProductMapper;
import com.example.ddd.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Use case for listing all active products.
 */
@Slf4j
@RequiredArgsConstructor
public class ListProductsUseCase {

    private final ProductRepository productRepository;

    public List<ProductResponse> execute() {
        log.debug("Fetching all active products");

        return productRepository.findAllActive().stream()
            .map(ProductMapper::toResponse)
            .collect(Collectors.toList());
    }
}
