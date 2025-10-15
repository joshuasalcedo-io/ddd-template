package com.example.ddd.infrastructure.persistence.events;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

/**
 * DomainEventEntity class.
 * chatter.chat-domain
 *
 * @author JoshuaSalcedo
 * @since 10/13/2025 7:40 AM
 */
@Entity
@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class DomainEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private  UUID eventId;


    private Instant occurredOn;

    private String eventType;


    private Long aggregateVersion;

    @ElementCollection(fetch = FetchType.LAZY)
    private Map<String,Object> metadata;

    private Object aggregateId;

}