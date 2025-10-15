package com.example.ddd.domain.event;

import com.example.ddd.domain.model.Money;
import com.example.ddd.domain.aggregateroot.product.ProductId;
import lombok.Getter;

import java.util.Map;

/**
 * Domain event emitted when a product is created.
 * Extends BaseDomainEvent to leverage common event infrastructure.
 */
@Getter
public class ProductCreatedEvent extends BaseDomainEvent<ProductId> {

    private final String productName;
    private final Money price;

    public ProductCreatedEvent(ProductId productId, String productName, Money price) {
        super(productId);
        this.productName = productName;
        this.price = price;
    }

    public ProductCreatedEvent(ProductId productId, String productName, Money price, Long aggregateVersion) {
        super(aggregateVersion, productId);
        this.productName = productName;
        this.price = price;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
            "productName", productName,
            "priceAmount", price.getAmount().toString(),
            "priceCurrency", price.getCurrency().getCurrencyCode()
        );
    }
}
