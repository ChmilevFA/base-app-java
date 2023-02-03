package net.chmilevfa.templates.base;

import net.chmilevfa.templates.base.action.TemplateActions;
import net.chmilevfa.templates.base.config.TemplateConfig;
import net.chmilevfa.templates.base.module.ServerModule;
import net.chmilevfa.templates.base.repository.DatabaseMigrator;
import net.chmilevfa.templates.base.repository.TemplateRepositories;
import net.chmilevfa.templates.base.utils.Yaml;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.net.MalformedURLException;
import java.net.URL;

import static java.lang.Runtime.getRuntime;

public class TemplateApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static final String CONFIG_FILE = "config.file";

    public final TemplateConfig config;
    public final TemplateRepositories repositories;
    public final TemplateActions actions;
    public final ServerModule serverModule;

    public TemplateApplication(TemplateConfig config) {
        this.config = config;
        this.repositories = new TemplateRepositories(config.ddlDbConfig);
        this.actions = new TemplateActions(repositories);
        this.serverModule = new ServerModule(config, repositories, actions);
    }

    public static void main(String[] args) throws MalformedURLException {
        final var configLocation = new URL(System.getProperty(CONFIG_FILE));
        final var templateConfig = Yaml.parse(configLocation, TemplateConfig.class);

        new TemplateApplication(templateConfig).start();
    }

    public void start() {
        LOG.info("Starting the application...");

        DatabaseMigrator.migrate(config.ddlDbConfig.url, config.ddlDbConfig.user, config.ddlDbConfig.password);
        serverModule.start();
        registerShutdownHook();

        LOG.info("Application successfully started");
    }

    private void registerShutdownHook() {
        getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Stopping the application");

            serverModule.stop();

            LOG.info("Application stopped");
        }));
    }
}
