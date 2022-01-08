package net.chmilevfa.templates.base;

import net.chmilevfa.templates.base.config.TemplateConfig;
import net.chmilevfa.templates.base.module.ServerModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static java.lang.Runtime.getRuntime;

public class TemplateApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public final TemplateConfig config;
    public final ServerModule serverModule;

    public TemplateApplication(TemplateConfig config) {
        this.config = config;

        LOG.info("Initializing http resources...");
        this.serverModule = new ServerModule(config);
    }

    public static void main(String[] args) {
        // TODO parse config from file
        new TemplateApplication(new TemplateConfig()).start();
    }

    public void start() {
        LOG.info("Starting the application...");

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
