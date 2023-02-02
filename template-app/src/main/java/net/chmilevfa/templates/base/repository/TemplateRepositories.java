package net.chmilevfa.templates.base.repository;

import net.chmilevfa.templates.base.config.DatabaseConfig;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class TemplateRepositories {

    public TemplateRepositories(DatabaseConfig config) {
    }

    private DataSource getDataSource(DatabaseConfig config) {
        final var datasource = new PGSimpleDataSource();
        datasource.setURL(config.url);
        datasource.setUser(config.user);
        datasource.setPassword(config.password);
        return datasource;
    }
}
