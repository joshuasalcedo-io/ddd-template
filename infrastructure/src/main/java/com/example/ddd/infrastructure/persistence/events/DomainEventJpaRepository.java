package com.example.ddd.infrastructure.persistence.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * DomainEventJpaRepository class.
 * chatter.chat-domain
 *
 * @author JoshuaSalcedo
 * @since 10/13/2025 7:45 AM
 */
@Repository
public interface DomainEventJpaRepository extends JpaRepository<DomainEventEntity, UUID> {

}
