package com.example.ddd.infrastructure.messaging;

import com.example.ddd.domain.event.ProductCreatedEvent;
import com.example.ddd.domain.event.ProductPriceChangedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * Event handler for product-related domain events.
 * This is where you would integrate with external systems (messaging, notifications, etc.)
 */
@Slf4j
@Component
public class ProductEventHandler {

    @EventListener
    public void handleProductCreated(ProductCreatedEvent event) {
        log.info("Product created event received: {} - {}",
            event.aggregateId(), event.getProductName());

        // TODO: Send notification, publish to message broker, update search index, etc.
    }

    @EventListener
    public void handleProductPriceChanged(ProductPriceChangedEvent event) {
        log.info("Product price changed event received: {} - {} -> {}",
            event.aggregateId(), event.getOldPrice(), event.getNewPrice());

        // TODO: Send notification, publish to message broker, update cache, etc.
    }
}
