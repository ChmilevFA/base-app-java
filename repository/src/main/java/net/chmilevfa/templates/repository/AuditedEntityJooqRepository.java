package net.chmilevfa.templates.repository;

import net.chmilevfa.templates.repository.connection.TransactionManager;
import net.chmilevfa.templates.repository.exception.OptimisticLockingRepositoryException;
import net.chmilevfa.templates.repository.model.AuditedEntity;
import org.jooq.Record;
import org.jooq.Table;
import org.jooq.TableField;

import java.time.Clock;
import java.time.LocalDateTime;

public abstract class AuditedEntityJooqRepository<E extends AuditedEntity<E, ID>, ID extends Comparable<ID>, R extends Record>
    extends EntityJooqRepository<E, ID, R> {

    protected final TableField<R, LocalDateTime> updatedDateField;

    protected AuditedEntityJooqRepository(TransactionManager transactionManager,
                                          Table<R> table,
                                          PrimaryKey<ID> primaryKey,
                                          TableField<R, LocalDateTime> updatedDateField) {
        super(transactionManager, table, primaryKey);
        this.updatedDateField = updatedDateField;
    }

    @Override
    public E update(E entity) {
        final var record = toRecord(entity);
        record.set(updatedDateField, LocalDateTime.now(Clock.systemUTC()));

        final var result = db.update(table)
            .set(record)
            .where(primaryKey.equal(entity.id)
                .and(updatedDateField.equal(entity.updatedDate)))
            .execute();
        if (result == 0) {
            throw new OptimisticLockingRepositoryException();
        }
        return get(entity.id);
    }
}
