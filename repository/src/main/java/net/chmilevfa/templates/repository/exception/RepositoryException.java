package net.chmilevfa.templates.repository.exception;

public abstract class RepositoryException extends RuntimeException {

    public RepositoryException() {

    }

    public RepositoryException(String message) {
        super(message);
    }

}
