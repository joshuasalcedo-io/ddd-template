package com.example.ddd.infrastructure.persistence.product;

import com.example.ddd.domain.aggregateroot.product.Product;
import com.example.ddd.domain.aggregateroot.product.ProductId;
import com.example.ddd.domain.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementation of ProductRepository using Spring Data JPA.
 * This adapter translates between domain and infrastructure layers.
 */
@Component
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository jpaRepository;

    @Override
    public Product save(Product product) {
        ProductEntity entity = ProductEntity.fromDomain(product);
        ProductEntity savedEntity = jpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<Product> findById(ProductId id) {
        return jpaRepository.findById(id.value())
            .map(ProductEntity::toDomain);
    }

    @Override
    public List<Product> findAll() {
        return List.of();
    }

    @Override
    public void delete(Product product) {

    }

    @Override
    public boolean existsById(ProductId id) {
        return jpaRepository.existsById(id.value());
    }

    @Override
    public void deleteById(ProductId id) {
        jpaRepository.deleteById(id.value());
    }

    @Override
    public List<Product> findByNameContaining(String searchTerm) {
        return jpaRepository.findByNameContainingIgnoreCase(searchTerm).stream()
            .map(ProductEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> findAllActive() {
        return jpaRepository.findAllActive().stream()
            .map(ProductEntity::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsByName(String name) {
        return jpaRepository.existsByName(name);
    }
}
