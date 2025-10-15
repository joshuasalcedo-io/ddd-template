package com.example.ddd.domain.event;

import com.example.ddd.domain.model.Money;
import com.example.ddd.domain.aggregateroot.product.ProductId;
import lombok.Getter;

import java.util.Map;

/**
 * Domain event emitted when a product price changes.
 * Extends BaseDomainEvent to leverage common event infrastructure.
 */
@Getter
public class ProductPriceChangedEvent extends BaseDomainEvent<ProductId> {

    private final Money oldPrice;
    private final Money newPrice;

    public ProductPriceChangedEvent(ProductId productId, Money oldPrice, Money newPrice) {
        super(productId);
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    public ProductPriceChangedEvent(ProductId productId, Money oldPrice, Money newPrice, Long aggregateVersion) {
        super(aggregateVersion, productId);
        this.oldPrice = oldPrice;
        this.newPrice = newPrice;
    }

    @Override
    public Map<String, Object> metadata() {
        return Map.of(
            "oldPriceAmount", oldPrice.getAmount().toString(),
            "oldPriceCurrency", oldPrice.getCurrency().getCurrencyCode(),
            "newPriceAmount", newPrice.getAmount().toString(),
            "newPriceCurrency", newPrice.getCurrency().getCurrencyCode()
        );
    }
}
