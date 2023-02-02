package net.chmilevfa.templates.base.module;

import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsPluginConfig;
import io.javalin.validation.JavalinValidation;
import net.chmilevfa.templates.base.config.TemplateConfig;
import net.chmilevfa.templates.base.http.HealthcheckResource;
import net.chmilevfa.templates.base.http.Resource;
import net.chmilevfa.templates.base.http.TemplateResource;
import net.chmilevfa.templates.base.repository.TemplateRepositories;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.Set;
import java.util.UUID;

public class ServerModule {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private final TemplateConfig config;
    private final Javalin server;
    private final TemplateRepositories repositories;

    public ServerModule(TemplateConfig config, TemplateRepositories repositories) {
        this.config = config;
        this.server = Javalin.create(javalinConfig ->
            javalinConfig.plugins.enableCors(cors ->
                cors.add(CorsPluginConfig::anyHost)));
        this.repositories = repositories;
        JavalinValidation.register(UUID.class, UUID::fromString);
    }

    public void start() {
        final var port = config.applicationPort;
        LOG.info("Starting HTTP server on port [{}] ...", port);

        registerResources();
        server.start(port);

        LOG.info("HTTP server started");
    }

    public void stop() {
        LOG.info("Stopping http server");

        server.stop();
    }

    private void registerResources() {
        getResources().forEach(r -> server.routes(r.routes()));
    }

    private Set<Resource> getResources() {
        return Set.of(
            new HealthcheckResource(),
            new TemplateResource()
        );
    }

}
