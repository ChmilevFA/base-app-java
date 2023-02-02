package net.chmilevfa.templates.repository.model;

import java.util.Objects;

public abstract class Model<M extends Model<M, S, ID>, S extends Enum<S>, ID extends Comparable<ID>> extends AuditedEntity<M, ID> {

    public final S state;

    protected Model(Builder<M, S, ID, ?> builder) {
        super(builder);
        this.state = builder.state;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final var model = (Model<?, ?, ?>) o;
        return Objects.equals(state, model.state);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), state);
    }

    public static abstract class Builder<M extends Model<M, S, ID>, S extends Enum<S>, ID extends Comparable<ID>, B extends Builder<M, S, ID, B>>
        extends AuditedEntity.Builder<M, ID, B> {

        protected S state;

        protected Builder() {
        }

        @SuppressWarnings("unchecked")
        public B state(S state) {
            this.state = state;
            return (B) this;
        }

        public abstract M build();
    }
}
