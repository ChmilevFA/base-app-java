package net.chmilevfa.templates.base;

import net.chmilevfa.templates.base.config.DatabaseConfig;
import net.chmilevfa.templates.base.config.TemplateConfig;
import org.testcontainers.containers.PostgreSQLContainer;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

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

    protected HttpRequest.Builder httpRequestBuilder(String path) {
        return HttpRequest.newBuilder(serverUri.resolve(path));
    }

    protected HttpResponse<String> sendHttp(HttpRequest request) {
        try {
            return HttpClient.newHttpClient().send(request, ofString());
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
