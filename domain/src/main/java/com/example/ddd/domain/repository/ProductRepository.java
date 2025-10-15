package com.example.ddd.domain.repository;

import com.example.ddd.domain.aggregateroot.product.Product;
import com.example.ddd.domain.aggregateroot.product.ProductId;
import org.jmolecules.ddd.annotation.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Product aggregate.
 * Defines domain-specific query methods.
 * Uses jMolecules Repository annotation to indicate repository pattern.
 */
@Repository
public interface ProductRepository {

    /**
     * Save a product.
     */
    Product save(Product product);

    /**
     * Find a product by its ID.
     */
    Optional<Product> findById(ProductId id);

    /**
     * Find all products.
     */
    List<Product> findAll();

    /**
     * Delete a product.
     */
    void delete(Product product);

    /**
     * Find all products with names containing the search term.
     */
    List<Product> findByNameContaining(String searchTerm);

    /**
     * Find all active products.
     */
    List<Product> findAllActive();

    /**
     * Check if a product with the given name exists.
     */
    boolean existsByName(String name);

    boolean existsById(ProductId id);

    void deleteById(ProductId id);


}
