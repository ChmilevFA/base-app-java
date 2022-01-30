package net.chmilevfa.templates.base;

import net.chmilevfa.templates.base.config.TemplateConfig;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpResponse.BodyHandlers.ofString;

public class FunctionalSpec {

    protected static final URI serverUri;

    static  {
        final var config = new TemplateConfig();
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
