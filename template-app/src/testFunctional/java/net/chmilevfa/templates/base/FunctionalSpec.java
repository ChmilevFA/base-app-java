package net.chmilevfa.templates.base;

import net.chmilevfa.templates.base.config.DatabaseConfig;
import net.chmilevfa.templates.base.config.TemplateConfig;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

import java.net.URI;

public class FunctionalSpec {

    private static final String POSTGRES_IMAGE = "postgres:15";
    private static final int APPLICATION_PORT = 7070;

    protected static final URI serverUri;

    static {
        final var dbContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE);
        dbContainer.start();

        final var config = new TemplateConfig(
            APPLICATION_PORT,
            new DatabaseConfig(dbContainer.getJdbcUrl(), dbContainer.getUsername(), dbContainer.getPassword()));

        serverUri = URI.create("http://localhost:" + config.applicationPort);

        final var application = new TemplateApplication(config);
        application.start();
    }

    protected static HttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }
}
