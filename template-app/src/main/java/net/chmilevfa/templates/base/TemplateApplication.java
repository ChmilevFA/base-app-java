package net.chmilevfa.templates.base;

import io.javalin.Javalin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

import static java.lang.Runtime.getRuntime;

public class TemplateApplication {

    private static final Logger LOG = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static Javalin server;

    public static void main(String[] args) {
        LOG.info("Starting the application...");

        server = Javalin.create().start(7000);
        server.get("/", ctx -> ctx.result("Hello World!"));

        registerShutdownHook();

        LOG.info("Application successfully started");

    }

    private static void registerShutdownHook() {
        getRuntime().addShutdownHook(new Thread(() -> {
            LOG.info("Stopping the application");
            server.close();
        }));
    }
}
