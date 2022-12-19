package net.chmilevfa.templates.base.config;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DatabaseConfig {
    public final String url;
    public final String user;
    public final String password;

    @JsonCreator
    public DatabaseConfig(@JsonProperty("url") String url,
                          @JsonProperty("user") String user,
                          @JsonProperty("password") String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
}
