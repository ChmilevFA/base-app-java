package net.chmilevfa.templates.base.repository;

import net.chmilevfa.templates.base.db.schema.tables.Users;
import net.chmilevfa.templates.base.db.schema.tables.records.UsersRecord;
import net.chmilevfa.templates.base.model.User;
import net.chmilevfa.templates.base.model.User.State;
import net.chmilevfa.templates.repository.ModelJooqRepository;
import net.chmilevfa.templates.repository.connection.TransactionManager;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.stream.Collectors.toUnmodifiableSet;
import static net.chmilevfa.templates.base.db.schema.tables.Users.USERS;
import static net.chmilevfa.templates.base.model.User.Builder.user;
import static net.chmilevfa.templates.base.model.User.State.DELETED;
import static net.chmilevfa.templates.repository.PrimaryKey.fieldPrimaryKey;

public class UserRepository extends ModelJooqRepository<User, State, UUID, UsersRecord> {

    UserRepository(TransactionManager transactionManager) {
        super(transactionManager, USERS, fieldPrimaryKey(USERS.ID), USERS.UPDATED_DATE, USERS.STATE, DELETED);
    }

    @Override
    protected User fromRecord(UsersRecord record) {
        return user()
            .id(record.getId())
            .createdDate(record.getCreatedDate())
            .updatedDate(record.getUpdatedDate())
            .state(State.valueOf(record.getState()))
            .username(record.getUsername())
            .password(record.getPassword())
            .email(record.getEmail())
            .build();
    }

    @Override
    protected UsersRecord toRecord(User entity) {
        return new UsersRecord(
            entity.id,
            entity.state.name(),
            entity.createdDate,
            entity.updatedDate,
            entity.username,
            entity.password,
            entity.email);
    }
}
