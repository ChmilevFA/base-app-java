package net.chmilevfa.templates.repository;

import org.jooq.Condition;
import org.jooq.TableField;

@FunctionalInterface
public interface PrimaryKey<ID extends Comparable<ID>> {

    Condition equal(ID id);

    static <ID extends Comparable<ID>> PrimaryKey<ID> fieldPrimaryKey(TableField<?, ID> tableField) {
        return tableField::equal;
    }
}
