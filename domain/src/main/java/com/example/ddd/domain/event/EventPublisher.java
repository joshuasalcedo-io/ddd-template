package com.example.ddd.domain.event;


import java.util.List;

/**
 * Port for publishing domain events.
 * This is an interface that will be implemented in the infrastructure layer.
 */
public interface EventPublisher {

    /**
     * Publish a domain event.
     */
    void publish(DomainEvent<?> event);

    void publish(List<DomainEvent<?>> events);

}
