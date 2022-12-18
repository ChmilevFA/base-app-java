package net.chmilevfa.templates.repository.exception;

public abstract class RepositoryException extends RuntimeException {

    public RepositoryException(String message) {
        super(message);
    }

}
