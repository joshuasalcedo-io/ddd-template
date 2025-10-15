package com.example.ddd.infrastructure.messaging;

import com.example.ddd.domain.event.DomainEvent;
import com.example.ddd.domain.event.EventPublisher;
import com.example.ddd.infrastructure.persistence.events.DomainEventEntity;
import com.example.ddd.infrastructure.persistence.events.DomainEventJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Implementation of EventPublisher using Spring's ApplicationEventPublisher.
 */
@Slf4j
@Component
public class SpringEventPublisher implements EventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final DomainEventJpaRepository domainEventJpaRepository;

    public SpringEventPublisher(ApplicationEventPublisher applicationEventPublisher, DomainEventJpaRepository domainEventJpaRepository) {
        this.applicationEventPublisher = applicationEventPublisher;
        this.domainEventJpaRepository = domainEventJpaRepository;
    }

    @Override
    public void publish(DomainEvent<?> event) {
        applicationEventPublisher.publishEvent(event);

        domainEventJpaRepository.save(toEntity(event));
           log.info("Published DomainEvent: {}", event);
    }
    @Override
    public void publish(List<DomainEvent<?>> events) {
        applicationEventPublisher.publishEvent(events);
        for (DomainEvent<?> event : events) {
            domainEventJpaRepository.save(toEntity(event));
        }

    }

    private DomainEventEntity toEntity(DomainEvent<?> domainEvent) {
  return  DomainEventEntity.builder()
                        .withAggregateId(domainEvent.aggregateId())
                        .withEventId(domainEvent.eventId())
                        .withMetadata(domainEvent.metadata())
                        .withOccurredOn(domainEvent.occurredOn())
                        .withAggregateVersion(domainEvent.aggregateVersion())

                .build();
    }
}
