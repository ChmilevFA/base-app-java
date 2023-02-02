package net.chmilevfa.templates.repository.exception;

import static java.lang.String.format;

public class NotFoundRepositoryException extends RepositoryException {

    public NotFoundRepositoryException(String table, Object id) {
        super(format("%s not found for %s", id, table));
    }
}
