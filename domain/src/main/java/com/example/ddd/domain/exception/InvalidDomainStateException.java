package com.example.ddd.domain.exception;

/**
 * Thrown when domain invariants are violated.
 */
public class InvalidDomainStateException extends DomainException {

    public InvalidDomainStateException(String message) {
        super(message);
    }
}
