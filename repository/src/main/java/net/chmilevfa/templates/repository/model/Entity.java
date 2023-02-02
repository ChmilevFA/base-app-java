package net.chmilevfa.templates.repository.model;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

import java.util.Objects;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

@SuppressWarnings("unchecked")
public abstract class Entity<E extends Entity<E, ID>, ID extends Comparable<ID>> {

    public final ID id;

    protected Entity(ID id) {
        this.id = id;
    }

    public Id<E, ID> id() {
        return Id.id((Class<E>) this.getClass(), id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Entity<?, ?> entity = (Entity<?, ?>) o;
        return Objects.equals(id, entity.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public String toString() {
        return ReflectionToStringBuilder.toString(this, SHORT_PREFIX_STYLE);
    }
}
