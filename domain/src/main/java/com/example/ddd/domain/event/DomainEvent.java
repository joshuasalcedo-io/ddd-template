package com.example.ddd.domain.event;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * DomainEvent class.
 * Base interface for all domain events in the system.
 * chatter-parent
 *
 * @author JoshuaSalcedo
 * @since 10/12/2025
 */
public interface DomainEvent<ID> {

    /**
     * Unique identifier for this event instance
     */
    UUID eventId();

    /**
     * Timestamp when the event occurred
     */
    Instant occurredOn();

    /**
     * Type of the event (typically the class name)
     */
    default String eventType() {
        return this.getClass().getSimpleName();
    }

    /**
     * Version of the aggregate that produced this event
     */
    default Long aggregateVersion() {
        return 1L;
    }


    Map<String, Object> metadata();
    ID aggregateId();
}
