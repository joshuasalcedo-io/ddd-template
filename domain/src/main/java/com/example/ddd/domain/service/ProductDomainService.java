package com.example.ddd.domain.service;

import com.example.ddd.domain.exception.InvalidDomainStateException;
import com.example.ddd.domain.model.Money;
import com.example.ddd.domain.aggregateroot.product.Product;
import org.jmolecules.ddd.annotation.Service;

/**
 * Domain service for Product-related business logic that doesn't fit
 * naturally within the Product aggregate.
 */
@Service
public class ProductDomainService {

    /**
     * Apply a discount to a product's price.
     * Business rule: Discount cannot exceed 50% of the original price.
     */
    public Money calculateDiscountedPrice(Product product, int discountPercentage) {
        if (discountPercentage < 0 || discountPercentage > 100) {
            throw new InvalidDomainStateException("Discount percentage must be between 0 and 100");
        }
        if (discountPercentage > 50) {
            throw new InvalidDomainStateException("Discount cannot exceed 50%");
        }

        Money originalPrice = product.getPrice();
        double discountFactor = 1.0 - (discountPercentage / 100.0);
        return Money.of(
            originalPrice.getAmount().multiply(java.math.BigDecimal.valueOf(discountFactor)),
            originalPrice.getCurrency()
        );
    }
}
