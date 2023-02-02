package net.chmilevfa.templates.repository.model;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.MICROS;

public abstract class AuditedEntity<E extends AuditedEntity<E, ID>, ID extends Comparable<ID>> extends Entity<E, ID> {

    public final LocalDateTime createdDate;
    public final LocalDateTime updatedDate;

    protected AuditedEntity(Builder<E, ID, ?> builder) {
        super(builder.id);
        this.createdDate = builder.createdDate;
        this.updatedDate = builder.updatedDate == null ? builder.createdDate : builder.updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final var that = (AuditedEntity<?, ?>) o;
        return Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(updatedDate, that.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), createdDate, updatedDate);
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<E extends Entity<E, ID>, ID extends Comparable<ID>, B extends Builder<E, ID, B>> {

        protected ID id;
        protected LocalDateTime createdDate = LocalDateTime.now(Clock.systemUTC()).truncatedTo(MICROS);
        protected LocalDateTime updatedDate;

        protected Builder() {
        }

        public B id(ID id) {
            this.id = id;
            return (B) this;
        }

        public B createdDate(LocalDateTime createdDate) {
            this.createdDate = createdDate;
            return (B) this;
        }

        public B updatedDate(LocalDateTime updatedDate) {
            this.updatedDate = updatedDate;
            return (B) this;
        }

        public abstract E build();
    }
}
