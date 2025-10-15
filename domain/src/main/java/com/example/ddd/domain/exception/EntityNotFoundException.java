package com.example.ddd.domain.exception;

/**
 * Thrown when an entity cannot be found.
 */
public class EntityNotFoundException extends DomainException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public static EntityNotFoundException forId(Class<?> entityType, Object id) {
        return new EntityNotFoundException(
            String.format("%s with ID %s not found", entityType.getSimpleName(), id)
        );
    }
}
