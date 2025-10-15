package com.example.ddd.infrastructure.persistence.product;

import com.example.ddd.domain.model.Money;
import com.example.ddd.domain.aggregateroot.product.Product;
import com.example.ddd.domain.aggregateroot.product.ProductId;
import com.example.ddd.domain.aggregateroot.product.ProductStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Currency;

/**
 * JPA Entity for Product.
 * This is the infrastructure representation of the Product aggregate.
 */
@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

    @Id
    @Column(name = "id", length = 36)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "price", nullable = false, precision = 19, scale = 2)
    private BigDecimal price;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @Column(name = "stock_quantity", nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    /**
     * Convert JPA entity to domain model.
     */
    public Product toDomain() {
        Money money = Money.of(this.price, Currency.getInstance(this.currency));
        return new Product(
            ProductId.of(this.id),
            this.name,
            this.description,
            money,
            this.stockQuantity,
            this.status,
            this.createdAt,
            this.updatedAt
        );
    }

    /**
     * Create JPA entity from domain model.
     */
    public static ProductEntity fromDomain(Product product) {
        return new ProductEntity(
            product.getId().value(),
            product.getName(),
            product.getDescription(),
            product.getPrice().getAmount(),
            product.getPrice().getCurrency().getCurrencyCode(),
            product.getStockQuantity(),
            product.getStatus(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
