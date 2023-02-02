package net.chmilevfa.templates.repository;

import net.chmilevfa.templates.repository.connection.TransactionManager;
import net.chmilevfa.templates.repository.exception.OptimisticLockingRepositoryException;
import net.chmilevfa.templates.repository.model.Model;
import org.jooq.Condition;
import org.jooq.Record;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.TableField;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;

public abstract class ModelJooqRepository<M extends Model<M, S, ID>, S extends Enum<S>, ID extends Comparable<ID>, R extends Record>
    extends AuditedEntityJooqRepository<M, ID, R> {

    protected final TableField<R, String> stateField;
    protected final Optional<Enum<S>> deletedState;

    protected ModelJooqRepository(TransactionManager transactionManager,
                                  Table<R> table,
                                  PrimaryKey<ID> primaryKey,
                                  TableField<R, LocalDateTime> updatedDateField,
                                  TableField<R, String> stateField) {
        super(transactionManager, table, primaryKey, updatedDateField);
        this.stateField = stateField;
        this.deletedState = empty();
    }

    protected ModelJooqRepository(TransactionManager transactionManager,
                                  Table<R> table,
                                  PrimaryKey<ID> primaryKey,
                                  TableField<R, LocalDateTime> updatedDateField,
                                  TableField<R, String> stateField,
                                  Enum<S> deletedState) {
        super(transactionManager, table, primaryKey, updatedDateField);
        this.stateField = stateField;
        this.deletedState = Optional.of(deletedState);
    }

    @Override
    public boolean delete(M model) {
        if (deletedState.isEmpty()) {
            throw new IllegalStateException(String.format("%s doesn't support DELETE operation", model.getClass().getSimpleName()));
        }

        final var record = toRecord(model);
        record.set(updatedDateField, LocalDateTime.now(Clock.systemUTC()));
        record.set(stateField, deletedState.get().name());

        final var result = db.update(table)
            .set(record)
            .where(primaryKey.equal(model.id)
                .and(updatedDateField.equal(model.updatedDate)))
            .execute();
        if (result == 0) {
            throw new OptimisticLockingRepositoryException();
        }
        return true;
    }

    @Override
    protected boolean existsWhere(Condition condition) {
        return deletedState
            .map(s -> super.existsWhere(condition.and(stateField.notEqual(s.name()))))
            .orElseGet(() -> super.existsWhere(condition));
    }

    @Override
    protected int countWhere(Condition condition) {
        return deletedState
            .map(s -> super.countWhere(condition.and(stateField.notEqual(s.name()))))
            .orElseGet(() -> super.countWhere(condition));
    }

    @Override
    protected Optional<M> findOneWhere(Condition condition) {
        return deletedState
            .map(s -> super.findOneWhere(condition.and(stateField.notEqual(s.name()))))
            .orElseGet(() -> super.findOneWhere(condition));
    }

    @Override
    protected List<M> findAllWhere(Condition condition) {
        return deletedState
            .map(s -> super.findAllWhere(condition.and(stateField.notEqual(s.name()))))
            .orElseGet(() -> super.findAllWhere(condition));
    }

    @Override
    protected List<M> findAllWhere(Condition condition, SortField<?> sortField) {
        return deletedState
            .map(s -> super.findAllWhere(condition.and(stateField.notEqual(s.name())), sortField))
            .orElseGet(() -> super.findAllWhere(condition, sortField));
    }
}
