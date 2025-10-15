package com.example.ddd.domain.event;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * BaseDomainEvent class.
 * Abstract base implementation for domain events.
 * chatter-parent
 *
 * @author JoshuaSalcedo
 * @since 10/12/2025
 */
@Getter
public abstract class BaseDomainEvent<ID> implements DomainEvent<ID> {

    private final UUID eventId;
    private final Instant occurredOn;
    private final Long aggregateVersion;
    private final ID aggregateId;

    protected BaseDomainEvent(ID  aggregateId) {
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.aggregateVersion = 1L;
        this.aggregateId = aggregateId;
    }

    protected BaseDomainEvent(Long aggregateVersion, ID aggregateId) {
        this.aggregateId = aggregateId;
        this.eventId = UUID.randomUUID();
        this.occurredOn = Instant.now();
        this.aggregateVersion = aggregateVersion;
    }

    @Override
    public UUID eventId() {
        return eventId;
    }

    @Override
    public Instant occurredOn() {
        return occurredOn;
    }

    @Override
    public Long aggregateVersion() {
        return aggregateVersion;
    }

    @Override
    public ID aggregateId() {
        return aggregateId;
    }
}
