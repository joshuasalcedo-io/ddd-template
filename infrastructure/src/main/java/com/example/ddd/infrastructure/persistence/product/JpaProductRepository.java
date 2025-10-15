package com.example.ddd.infrastructure.persistence.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data JPA repository interface for ProductEntity.
 */
@Repository
public interface JpaProductRepository extends JpaRepository<ProductEntity, String> {

    List<ProductEntity> findByNameContainingIgnoreCase(String searchTerm);

    @Query("SELECT p FROM ProductEntity p WHERE p.status = 'ACTIVE'")
    List<ProductEntity> findAllActive();

    boolean existsByName(String name);
}
