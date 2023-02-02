package net.chmilevfa.templates.repository;

import net.chmilevfa.templates.repository.model.Entity;
import net.chmilevfa.templates.repository.model.Id;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static java.util.Collections.unmodifiableCollection;

@SuppressWarnings("rawtypes")
public class Batch implements Iterable<Batch.Change> {

    private final Map<Id, Change> changes = new LinkedHashMap<>();
    private boolean closed;

    public <E extends Entity> E add(E entity) {
        addToBatch(ChangeType.ADD, entity);
        return entity;
    }

    public <E extends Entity> E update(E entity) {
        addToBatch(ChangeType.UPDATE, entity);
        return entity;
    }

    public <E extends Entity> E delete(E entity) {
        addToBatch(ChangeType.DELETE, entity);
        return entity;
    }

    public void close() {
        this.closed = true;
    }

    private <E extends Entity> void addToBatch(ChangeType changeType, E entity) {
        if (closed) {
            throw new IllegalStateException("This batch can't be changed");
        }

        final var previous = changes.put(entity.id(), new Change<>(changeType, entity));
        if (previous != null) {
            throw new IllegalStateException("Duplicated item in the batch");
        }
    }

    @Override
    public Iterator<Change> iterator() {
        return unmodifiableCollection(changes.values()).iterator();
    }

    public enum ChangeType {
        ADD,
        UPDATE,
        DELETE
    }

    public static class Change<E extends Entity> {

        public final ChangeType type;
        public final E entity;

        public Change(ChangeType type, E entity) {
            this.type = type;
            this.entity = entity;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            final Change<?> change = (Change<?>) o;
            return type == change.type &&
                Objects.equals(entity, change.entity);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, entity);
        }
    }
}
