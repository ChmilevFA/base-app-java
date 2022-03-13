package net.chmilevfa.templates.base.repository;

import net.chmilevfa.templates.base.config.DatabaseConfig;
import org.postgresql.ds.PGSimpleDataSource;

import javax.sql.DataSource;

public class TemplateRepositories {

    public final ArticleRepository articleRepository;

    public TemplateRepositories(DatabaseConfig config) {
        this.articleRepository = new ArticleRepository(getDataSource(config));
    }

    private DataSource getDataSource(DatabaseConfig config) {
        final var datasource = new PGSimpleDataSource();
        datasource.setURL(config.url);
        datasource.setUser(config.user);
        datasource.setPassword(config.password);
        return datasource;
    }
}
