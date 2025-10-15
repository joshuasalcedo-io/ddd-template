package com.example.ddd.domain.aggregateroot.product;

import com.example.ddd.domain.event.DomainEvent;
import com.example.ddd.domain.event.ProductCreatedEvent;
import com.example.ddd.domain.event.ProductPriceChangedEvent;
import com.example.ddd.domain.exception.InvalidDomainStateException;
import com.example.ddd.domain.model.Money;
import lombok.Getter;
import org.jmolecules.ddd.annotation.Identity;
import org.jmolecules.ddd.types.AggregateRoot;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/**
 * Product aggregate root.
 * Represents a product in the catalog.
 * Uses jMolecules AggregateRoot interface with ProductId as the identifier type.
 */
@Getter
@org.jmolecules.ddd.annotation.AggregateRoot
public class Product implements AggregateRoot<Product, ProductId> {
    @Identity
    private final ProductId id;
    private String name;
    private String description;
    private Money price;
    private int stockQuantity;
    private ProductStatus status;
    private final Instant createdAt;
    private Instant updatedAt;

    // Domain events collection
    private final transient List<DomainEvent<?>> domainEvents = new ArrayList<>();

    // Constructor for creating new products
    private Product(ProductId id, String name, String description, Money price, int stockQuantity) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = ProductStatus.ACTIVE;
        this.createdAt = Instant.now();
        this.updatedAt = this.createdAt;

        // Register domain event
        registerEvent(new ProductCreatedEvent(id, name, price));
    }

    // Factory method for creating new products
    public static Product create(String name, String description, Money price, int initialStock) {
        validateProductData(name, price, initialStock);
        return new Product(ProductId.generate(), name, description, price, initialStock);
    }

    // Reconstitution constructor (for loading from DB)
    public Product(ProductId id, String name, String description, Money price,
                   int stockQuantity, ProductStatus status, Instant createdAt, Instant updatedAt) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Domain event management methods
    protected void registerEvent(DomainEvent<?> event) {
        this.domainEvents.add(event);
    }

    public List<DomainEvent<?>> getDomainEvents() {
        return List.copyOf(domainEvents);
    }

    public void clearDomainEvents() {
        this.domainEvents.clear();
    }

    // Business methods
    public void changePrice(Money newPrice) {
        if (newPrice == null) {
            throw new InvalidDomainStateException("Price cannot be null");
        }
        Money oldPrice = this.price;
        this.price = newPrice;
        this.updatedAt = Instant.now();

        // Register domain event
        registerEvent(new ProductPriceChangedEvent(getId(), oldPrice, newPrice));
    }

    public void updateInfo(String name, String description) {
        validateName(name);
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
    }

    public void addStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidDomainStateException("Quantity to add must be positive");
        }
        this.stockQuantity += quantity;
        this.updatedAt = Instant.now();
    }

    public void removeStock(int quantity) {
        if (quantity <= 0) {
            throw new InvalidDomainStateException("Quantity to remove must be positive");
        }
        if (this.stockQuantity < quantity) {
            throw new InvalidDomainStateException(
                String.format("Insufficient stock. Available: %d, Requested: %d",
                    this.stockQuantity, quantity)
            );
        }
        this.stockQuantity -= quantity;
        this.updatedAt = Instant.now();
    }

    public void activate() {
        this.status = ProductStatus.ACTIVE;
        this.updatedAt = Instant.now();
    }

    public void deactivate() {
        this.status = ProductStatus.INACTIVE;
        this.updatedAt = Instant.now();
    }

    public boolean isAvailable() {
        return this.status == ProductStatus.ACTIVE && this.stockQuantity > 0;
    }

    // Validation methods
    private static void validateProductData(String name, Money price, int stockQuantity) {
        validateName(name);
        if (price == null) {
            throw new InvalidDomainStateException("Price cannot be null");
        }
        if (stockQuantity < 0) {
            throw new InvalidDomainStateException("Stock quantity cannot be negative");
        }
    }

    private static void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new InvalidDomainStateException("Product name cannot be null or blank");
        }
        if (name.length() > 255) {
            throw new InvalidDomainStateException("Product name cannot exceed 255 characters");
        }
    }
}
