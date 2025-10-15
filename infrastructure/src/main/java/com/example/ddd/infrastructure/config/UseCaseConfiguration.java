package com.example.ddd.infrastructure.config;

import com.example.ddd.domain.event.EventPublisher;
import com.example.ddd.application.usecase.*;
import com.example.ddd.domain.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration for application use cases.
 * Creates beans for all use cases with their dependencies.
 */
@Configuration
public class UseCaseConfiguration {

    @Bean
    public CreateProductUseCase createProductUseCase(
            ProductRepository productRepository,
            EventPublisher eventPublisher) {
        return new CreateProductUseCase(productRepository, eventPublisher);
    }

    @Bean
    public GetProductUseCase getProductUseCase(ProductRepository productRepository) {
        return new GetProductUseCase(productRepository);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(
            ProductRepository productRepository,
            EventPublisher eventPublisher) {
        return new UpdateProductUseCase(productRepository, eventPublisher);
    }

    @Bean
    public ListProductsUseCase listProductsUseCase(ProductRepository productRepository) {
        return new ListProductsUseCase(productRepository);
    }
}
