package net.chmilevfa.templates.base.config;

// TODO: deserialize from file config
public class TemplateConfig {
    public final int applicationPort;
    public final DatabaseConfig ddlDbConfig;

    public TemplateConfig() {
        applicationPort = 7000;
        ddlDbConfig = new DatabaseConfig("jdbc:postgresql://localhost:5433/template", "ddl-template", "password");
    }

    public TemplateConfig(int applicationPort, DatabaseConfig ddlDbConfig) {
        this.applicationPort = applicationPort;
        this.ddlDbConfig = ddlDbConfig;
    }
}
