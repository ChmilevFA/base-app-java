package net.chmilevfa.templates.base.model;

import net.chmilevfa.templates.repository.model.Model;

import java.util.Objects;
import java.util.UUID;

import static java.util.UUID.randomUUID;

/**
 * Passwords should not be passed and stored as is.
 * Due to security reasons it's out of the scope of the template.
 */
public class User extends Model<User, User.State, UUID> {

    public final String username;
    public final String password;
    public final String email;

    private User(Builder builder) {
        super(builder);
        this.username = builder.username;
        this.password = builder.password;
        this.email = builder.email;
    }

    public enum State {
        ACTIVE,
        DELETED
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), username, password, email);
    }

    public static class Builder extends Model.Builder<User, State, UUID, Builder> {

        private String username;
        private String password;
        private String email;

        private Builder() {
        }

        public static Builder user() {
            return new Builder().id(randomUUID());
        }

        public Builder username(String username) {
            this.username = username;
            return this;
        }

        public Builder password(String password) {
            this.password = password;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        @Override
        public User build() {
            return new User(this);
        }
    }
}
