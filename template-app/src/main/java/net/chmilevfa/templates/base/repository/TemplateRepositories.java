package net.chmilevfa.templates.base.repository;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import net.chmilevfa.templates.base.config.DatabaseConfig;
import net.chmilevfa.templates.repository.connection.HikariConnectionProvider;
import net.chmilevfa.templates.repository.connection.TransactionManager;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

import static java.util.concurrent.TimeUnit.MINUTES;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TemplateRepositories {

    public final UserRepository userRepository;

    public TemplateRepositories(DatabaseConfig config) {
        final var dataSourceConnectionProvider = new HikariConnectionProvider(dataSource(config));
        final var transactionManager = new TransactionManager(dataSourceConnectionProvider);
        this.userRepository = new UserRepository(transactionManager);
    }

    private HikariDataSource dataSource(DatabaseConfig config) {
        final var hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(config.driverClass);
        hikariConfig.setJdbcUrl(config.url);
        hikariConfig.setUsername(config.user);
        hikariConfig.setPassword(config.password);
        hikariConfig.setMinimumIdle(1);
        hikariConfig.setMaximumPoolSize(10);
        hikariConfig.setConnectionTimeout(SECONDS.toMillis(1));
        hikariConfig.setIdleTimeout(MINUTES.toMillis(15));
        hikariConfig.setMaxLifetime(MINUTES.toMillis(30));
        hikariConfig.setLeakDetectionThreshold(SECONDS.toMillis(5));
        hikariConfig.setTransactionIsolation("TRANSACTION_READ_COMMITTED");
        return new HikariDataSource(hikariConfig);
    }
}
