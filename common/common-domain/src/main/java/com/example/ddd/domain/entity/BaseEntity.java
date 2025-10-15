package com.example.ddd.domain.entity;


import java.io.Serializable;
import java.util.Objects;

/**
 * BaseEntity class.
 * ddd-template
 *
 * @author JoshuaSalcedo
 * @since 10/15/2025 4:56 PM
 */
public abstract class BaseEntity<ID>{
    private ID id;

    public ID getId() {
        return id;
    }
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BaseEntity<?> that = (BaseEntity<?>) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
}
