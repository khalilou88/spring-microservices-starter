package com.example.core.vault.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "spring.cloud.vault")
public class VaultConfig {

    private String uri = "http://localhost:8200";
    private String token;
    private Kv kv = new Kv();
    private Database database = new Database();

    public String getUri() { return uri; }
    public void setUri(String uri) { this.uri = uri; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public Kv getKv() { return kv; }
    public void setKv(Kv kv) { this.kv = kv; }

    public Database getDatabase() { return database; }
    public void setDatabase(Database database) { this.database = database; }

    public static class Kv {
        private String backend = "secret";
        private String defaultContext = "application";

        public String getBackend() { return backend; }
        public void setBackend(String backend) { this.backend = backend; }

        public String getDefaultContext() { return defaultContext; }
        public void setDefaultContext(String defaultContext) { this.defaultContext = defaultContext; }
    }

    public static class Database {
        private String role;
        private String backend = "database";

        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }

        public String getBackend() { return backend; }
        public void setBackend(String backend) { this.backend = backend; }
    }
}