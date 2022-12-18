package net.chmilevfa.templates.repository.exception;

import static java.lang.String.format;

public class NotFoundException extends RepositoryException {

    public NotFoundException(String table, Object id) {
        super(format("%s not found for %s", id, table));
    }
}
