package net.chmilevfa.templates.base.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DatabaseConfig {

    public final String driverClass;
    public final String url;
    public final String user;
    public final String password;

    @JsonCreator
    public DatabaseConfig(@JsonProperty("driverClass") String driverClass,
                          @JsonProperty("url") String url,
                          @JsonProperty("user") String user,
                          @JsonProperty("password") String password) {
        this.driverClass = driverClass;
        this.url = url;
        this.user = user;
        this.password = password;
    }
}
