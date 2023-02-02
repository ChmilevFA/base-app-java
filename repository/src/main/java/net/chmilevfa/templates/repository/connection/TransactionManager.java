package net.chmilevfa.templates.repository.connection;

import net.chmilevfa.templates.repository.exception.OptimisticLockingRepositoryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

public class TransactionManager implements ConnectionProvider {

    private static final Logger LOG = LoggerFactory.getLogger(TransactionManager.class);

    private final ConnectionProvider delegate;
    private final ThreadLocal<Connection> localConnection = new ThreadLocal<>();

    public TransactionManager(ConnectionProvider delegate) {
        this.delegate = delegate;
    }

    public void inTransaction(Runnable runnable) {
        final var localConnection = initOrGetLocalConnection();
        try {
            runnable.run();
            localConnection.commit();
        } catch (OptimisticLockingRepositoryException e) {
            rollback(localConnection);
            throw e;
        } catch (Exception e) {
            LOG.error("Exception during transaction execution", e);
            rollback(localConnection);
            throw new RuntimeException(e);
        } finally {
            releaseLocalConnection();
        }
    }

    @Override
    public Connection acquire() throws SQLException {
        final var localConnection = this.localConnection.get();
        if (localConnection != null) {
            return localConnection;
        }

        return delegate.acquire();
    }

    @Override
    public void release(Connection connection) throws SQLException {
        final var localConnection = this.localConnection.get();
        if (localConnection == null || localConnection != connection) {
            delegate.release(connection);
        }
    }

    private Connection initOrGetLocalConnection() {
        if (localConnection.get() != null) {
            return localConnection.get();
        }

        try {
            final var connection = delegate.acquire();
            connection.setAutoCommit(false);
            localConnection.set(connection);
            return connection;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void rollback(Connection connection) {
        try {
            connection.rollback();
        } catch (SQLException e) {
            LOG.error("Exception while performing rollback", e);
            throw new RuntimeException(e);
        }
    }

    private void releaseLocalConnection() {
        if (localConnection.get() == null) {
            return;
        }

        try {
            final var connection = localConnection.get();
            localConnection.set(null);
            delegate.release(connection);
        } catch (SQLException e) {
            LOG.error("Exception while releasing connection", e);
            throw new RuntimeException(e);
        }
    }
}
