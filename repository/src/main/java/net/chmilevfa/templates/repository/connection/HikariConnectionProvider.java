package net.chmilevfa.templates.repository.connection;

import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class HikariConnectionProvider implements ConnectionProvider {

    private final HikariDataSource datasource;

    public HikariConnectionProvider(HikariDataSource datasource) {
        this.datasource = datasource;
    }

    @Override
    public Connection acquire() throws SQLException {
        return datasource.getConnection();
    }

    @Override
    public void release(Connection connection) throws SQLException {
        connection.close();
    }
}
