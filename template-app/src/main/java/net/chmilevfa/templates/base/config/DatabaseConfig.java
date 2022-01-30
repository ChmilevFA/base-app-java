package net.chmilevfa.templates.base.config;

public class DatabaseConfig {
    public final String url;
    public final String user;
    public final String password;

    public DatabaseConfig(String url, String user, String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }
}
