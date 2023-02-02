package net.chmilevfa.templates.repository;

import net.chmilevfa.templates.repository.connection.TransactionManager;
import net.chmilevfa.templates.repository.exception.NotFoundRepositoryException;
import net.chmilevfa.templates.repository.exception.OptimisticLockingRepositoryException;
import net.chmilevfa.templates.repository.model.Entity;
import org.jooq.Condition;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.SortField;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static org.jooq.impl.DSL.trueCondition;

public abstract class EntityJooqRepository<E extends Entity<E, ID>, ID extends Comparable<ID>, R extends Record> implements Repository<E, ID> {

    protected final DSLContext db;
    protected final Table<R> table;
    protected final PrimaryKey<ID> primaryKey;

    protected EntityJooqRepository(TransactionManager transactionManager,
                                   Table<R> table,
                                   PrimaryKey<ID> primaryKey) {
        this.db = DSL.using(new DefaultConfiguration()
            .set(new JooqConnectionProvider(transactionManager))
            .set(SQLDialect.POSTGRES));
        this.table = table;
        this.primaryKey = primaryKey;
    }

    @Override
    public E add(E entity) {
        final var result = db.insertInto(table)
            .set(toRecord(entity))
            .returning()
            .fetchOne();
        return fromRecord(result);
    }

    @Override
    public E get(ID id) {
        return getOneWhere(primaryKey.equal(id));
    }

    @Override
    public Optional<E> find(ID id) {
        return findOneWhere(primaryKey.equal(id));
    }

    @Override
    public List<E> findAll() {
        return findAllWhere(trueCondition());
    }

    @Override
    public E update(E entity) {
        final var result = db.update(table)
            .set(toRecord(entity))
            .where(primaryKey.equal(entity.id))
            .execute();
        if (result == 0) {
            throw new OptimisticLockingRepositoryException();
        }
        return get(entity.id);
    }

    @Override
    public boolean exists(ID id) {
        return existsWhere(primaryKey.equal(id));
    }

    @Override
    public boolean delete(E entity) {
        final var deletedCount = db.delete(table)
            .where(primaryKey.equal(entity.id))
            .execute();
        if (deletedCount > 1) {
            throw new IllegalStateException(format("Removed %s rows by one id = %s", deletedCount, entity.id));
        }
        return deletedCount == 1;
    }

    protected abstract E fromRecord(R record);

    protected abstract R toRecord(E entity);

    protected boolean existsWhere(Condition condition) {
        return db.fetchExists(table, condition);
    }

    protected int countWhere(Condition condition) {
        return db.fetchCount(table, condition);
    }

    protected E getOneWhere(Condition condition) {
        return findOneWhere(condition)
            .orElseThrow(() -> new NotFoundRepositoryException(table.getName(), condition.toString()));
    }

    protected Optional<E> findOneWhere(Condition condition) {
        final var result = db.selectFrom(table)
            .where(condition)
            .fetch();
        if (result.size() > 1) {
            throw new IllegalStateException(format("Expected 1 record, but received %s. Check your query: %s",
                result.size(), condition.toString()));
        }

        return result.stream()
            .map(this::fromRecord)
            .findFirst();
    }

    protected List<E> findAllWhere(Condition condition) {
        return db.selectFrom(table)
            .where(condition)
            .fetch()
            .stream()
            .map(this::fromRecord)
            .toList();
    }

    protected List<E> findAllWhere(Condition condition, SortField<?> sortField) {
        return db.selectFrom(table)
            .where(condition)
            .orderBy(sortField)
            .fetch()
            .stream()
            .map(this::fromRecord)
            .toList();
    }
}
