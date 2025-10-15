package com.example.ddd.application.mapper;

import com.example.ddd.application.dto.CreateProductRequest;
import com.example.ddd.application.dto.ProductResponse;
import com.example.ddd.domain.model.Money;
import com.example.ddd.domain.aggregateroot.product.Product;

import java.util.Currency;

/**
 * Mapper for converting between Product domain objects and DTOs.
 */
public class ProductMapper {

    public static Product toDomain(CreateProductRequest request) {
        Money price = Money.of(request.price(), Currency.getInstance(request.currency()));
        return Product.create(
            request.name(),
            request.description(),
            price,
            request.initialStock()
        );
    }

    public static ProductResponse toResponse(Product product) {
        return new ProductResponse(
            product.getId().value(),
            product.getName(),
            product.getDescription(),
            product.getPrice().getAmount(),
            product.getPrice().getCurrency().getCurrencyCode(),
            product.getStockQuantity(),
            product.getStatus().name(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
