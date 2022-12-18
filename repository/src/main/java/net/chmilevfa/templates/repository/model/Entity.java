package net.chmilevfa.templates.repository.model;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class Entity<E extends Entity<E, ID, B>, ID, B extends Entity.Builder<E, ID, B>> {

    public final ID id;
    public final LocalDateTime createdDate;
    public final LocalDateTime updatedDate;

    public Entity(B builder) {
        this.id = builder.id;
        this.createdDate = builder.createdDate;
        this.updatedDate = builder.updatedDate == null ? builder.createdDate : builder.updatedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity<?, ?, ?> entity = (Entity<?, ?, ?>) o;
        return Objects.equals(id, entity.id) && Objects.equals(createdDate, entity.createdDate) && Objects.equals(updatedDate, entity.updatedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, updatedDate);
    }

    @SuppressWarnings("unchecked")
    public static abstract class Builder<E extends Entity<E, ID, B>, ID, B extends Builder<E, ID, B>> {

        protected ID id;
        protected LocalDateTime createdDate = LocalDateTime.now(Clock.systemUTC());
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
