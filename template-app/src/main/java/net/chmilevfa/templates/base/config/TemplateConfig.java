package net.chmilevfa.templates.base.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TemplateConfig {
    public final int applicationPort;
    public final boolean enableSwaggerUI;
    public final DatabaseConfig ddlDbConfig;

    @JsonCreator
    public TemplateConfig(@JsonProperty("port") int applicationPort,
                          @JsonProperty("enableSwaggerUI") boolean enableSwaggerUI,
                          @JsonProperty("database") DatabaseConfig ddlDbConfig) {
        this.applicationPort = applicationPort;
        this.enableSwaggerUI = enableSwaggerUI;
        this.ddlDbConfig = ddlDbConfig;
    }
}
