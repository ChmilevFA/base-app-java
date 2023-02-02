package net.chmilevfa.templates.repository;

import net.chmilevfa.templates.repository.connection.TransactionManager;
import org.jooq.exception.DataAccessException;

import java.sql.Connection;
import java.sql.SQLException;

public class JooqConnectionProvider implements org.jooq.ConnectionProvider {

    private final TransactionManager transactionManager;

    public JooqConnectionProvider(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    @Override
    public Connection acquire() throws DataAccessException {
        try {
            return transactionManager.acquire();
        } catch (SQLException e) {
            throw new DataAccessException("", e);
        }
    }

    @Override
    public void release(Connection connection) throws DataAccessException {
        try {
            transactionManager.release(connection);
        } catch (SQLException e) {
            throw new DataAccessException("", e);
        }
    }
}
