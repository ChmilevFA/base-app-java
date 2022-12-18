package net.chmilevfa.templates.repository;

import net.chmilevfa.templates.repository.exception.NotFoundException;
import net.chmilevfa.templates.repository.model.Entity;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.SQLDialect;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.impl.DSL;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;

import javax.sql.DataSource;
import java.util.List;

import static java.lang.String.format;

public abstract class SimpleJooqRepository<E extends Entity<E, ID, ?>, ID, R extends Record> implements Repository<E, ID> {

    private final DSLContext db;
    private final Table<R> table;
    private final TableField<R, ID> idField;

    protected SimpleJooqRepository(DataSource dataSource,
                                   Table<R> table,
                                   TableField<R, ID> idField) {
        this.db = DSL.using(new DefaultConfiguration()
            .set(new DataSourceConnectionProvider(dataSource))
            .set(SQLDialect.POSTGRES));
        this.table = table;
        this.idField = idField;
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
        final var result = db.selectFrom(table)
            .where(idField.equal(id))
            .fetchOne();
        if (result == null) {
            throw new NotFoundException(table.getName(), id);
        }
        return fromRecord(result);
    }

    @Override
    public List<E> findAll() {
        return db.selectFrom(table)
            .fetch().stream()
            .map(this::fromRecord)
            .toList();
    }

    @Override
    public E update(E entity) {
        final var result = db.update(table)
            .set(toRecord(entity))
            .where(idField.equal(entity.id))
            .execute();
        if (result == 0) {
            throw new NotFoundException(table.getName(), entity.id);
        }
        return get(entity.id);
    }

    @Override
    public boolean exists(ID id) {
        return db.fetchExists(table, idField.equal(id));
    }

    @Override
    public boolean delete(ID id) {
        final var deletedCount = db.delete(table)
            .where(idField.equal(id))
            .execute();
        if (deletedCount > 1) {
            throw new IllegalStateException(format("Removed %s rows by one id = %s", deletedCount, id));
        }
        return deletedCount == 1;
    }

    protected abstract E fromRecord(R record);

    protected abstract R toRecord(E entity);
}
