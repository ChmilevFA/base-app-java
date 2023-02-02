package net.chmilevfa.templates.repository.model;

import java.util.Objects;

public class Id<E extends Entity<E, ID>, ID extends Comparable<ID>> {

    public final Class<E> type;
    public final ID id;

    private Id(Class<E> type, ID id) {
        this.type = type;
        this.id = id;
    }

    public static <E extends Entity<E, ID>, ID extends Comparable<ID>> Id<E, ID> id(Class<E> type, ID id) {
        return new Id<>(type, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Id<?, ?> id1 = (Id<?, ?>) o;
        return Objects.equals(type, id1.type)
            && Objects.equals(id, id1.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, id);
    }

    @Override
    public String toString() {
        return type.getSimpleName() + "#" + id;
    }
}