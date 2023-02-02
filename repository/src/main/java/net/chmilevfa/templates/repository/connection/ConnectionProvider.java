package net.chmilevfa.templates.repository.connection;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionProvider {

    Connection acquire() throws SQLException;

    void release(Connection connection) throws SQLException;
}
