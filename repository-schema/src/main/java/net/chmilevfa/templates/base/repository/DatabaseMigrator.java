package net.chmilevfa.templates.base.repository;

import org.flywaydb.core.Flyway;

public class DatabaseMigrator {

    static final String MIGRATION_PACKAGE = "classpath:net/chmilevfa/templates/base/db/migration";

    private final Flyway flyway;

    public DatabaseMigrator(String url, String user, String password) {
        this.flyway = Flyway.configure()
            .dataSource(url, user, password)
            .locations(MIGRATION_PACKAGE)
            .load();
    }

    private void migrate() {
        this.flyway.migrate();
    }

    public static void migrate(String url, String user, String password) {
        new DatabaseMigrator(url, user, password).migrate();
    }
}
