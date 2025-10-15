package com.example.ddd.domain.valueobject;


import lombok.Getter;

import java.util.Objects;

/**
 * BaseId class.
 * ddd-template
 *
 * @author JoshuaSalcedo
 * @since 10/15/2025 5:00 PM
 */

@Getter
public abstract class BaseId<T> {
    private final T value;
    public BaseId(T value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseId<?> baseId = (BaseId<?>) o;
        return Objects.equals(getValue(), baseId.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }
}