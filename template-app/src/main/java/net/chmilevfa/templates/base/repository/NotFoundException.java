package net.chmilevfa.templates.base.repository;

import static java.lang.String.format;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String table, Object id) {
        super(format("%s not found for %s", id, table));
    }
}
