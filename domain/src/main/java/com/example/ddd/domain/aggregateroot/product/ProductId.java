package com.example.ddd.domain.aggregateroot.product;

import org.jmolecules.ddd.types.Identifier;

import java.util.UUID;

/**
 * Value object representing a Product ID.
 * Uses jMolecules Identifier interface to represent a domain identifier.
 */
public record ProductId(String value) implements Identifier {

    public ProductId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Product ID cannot be null or blank");
        }
    }

    public static ProductId of(String value) {
        return new ProductId(value);
    }

    public static ProductId generate() {
        return new ProductId(UUID.randomUUID().toString().replaceAll("-", ""));
    }

}
