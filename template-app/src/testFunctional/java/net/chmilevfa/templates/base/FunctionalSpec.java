package net.chmilevfa.templates.base;

import net.chmilevfa.templates.base.config.DatabaseConfig;
import net.chmilevfa.templates.base.config.TemplateConfig;
import net.chmilevfa.templates.base.utils.Yaml;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.testcontainers.containers.PostgreSQLContainer;

import java.net.URI;

public class FunctionalSpec {

    private static final String POSTGRES_IMAGE = "postgres:15";

    protected static final URI serverUri;

    static {
        final var dbContainer = new PostgreSQLContainer<>(POSTGRES_IMAGE);
        dbContainer.start();

        final var config = withConfigOverride(Yaml.parse(FunctionalSpec.class.getResource("/config.yml"), TemplateConfig.class), dbContainer);

        serverUri = URI.create("http://localhost:" + config.applicationPort);

        final var application = new TemplateApplication(config);
        application.start();
    }

    protected static HttpClient httpClient() {
        return HttpClientBuilder.create().build();
    }

    private static TemplateConfig withConfigOverride(TemplateConfig config, PostgreSQLContainer<?> dbContainer) {
        return new TemplateConfig(
            config.applicationPort,
            new DatabaseConfig(
                dbContainer.getDriverClassName(),
                dbContainer.getJdbcUrl(),
                dbContainer.getUsername(),
                dbContainer.getPassword()));
    }
}
