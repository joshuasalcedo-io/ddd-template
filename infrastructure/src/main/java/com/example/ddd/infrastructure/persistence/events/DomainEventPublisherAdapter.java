package com.example.ddd.infrastructure.persistence.events;


import com.example.ddd.domain.event.DomainEvent;
import com.example.ddd.domain.event.EventPublisher;
import org.jmolecules.event.annotation.DomainEventPublisher;

import java.util.List;

/**
 * DomainEventPublisherAdapter class.
 * ddd-template
 *
 * @author JoshuaSalcedo
 * @since 10/15/2025 3:48 PM
 */

public class DomainEventPublisherAdapter  implements EventPublisher {

    private final DomainEventJpaRepository domainEventJpaRepository;

    public DomainEventPublisherAdapter(DomainEventJpaRepository domainEventJpaRepository) {
        this.domainEventJpaRepository = domainEventJpaRepository;
    }


}