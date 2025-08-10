package com.example.core.testing.containers;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.vault.VaultContainer;

@TestConfiguration(proxyBeanMethods = false)
@Profile("test")
public class TestContainersConfiguration {

    // Static containers for @DynamicPropertySource access
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
                    DockerImageName.parse("postgres:15-alpine"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);

    static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0")).withReuse(true);

    static VaultContainer<?> vaultContainer = new VaultContainer<>(DockerImageName.parse("hashicorp/vault:latest"))
            .withVaultToken("test-token")
            .withVaultPort(8200)
            .withSecretInVault("secret/application", "spring.datasource.password", "test")
            .withReuse(true);

    // Bean definitions for container lifecycle management
//    @Bean
//    public PostgreSQLContainer<?> postgresContainer() {
//        return postgresContainer;
//    }

    static {
        postgresContainer.start(); // Ensure DB ready before context builds
    }

    @Bean
    public KafkaContainer kafkaContainer() {
        return kafkaContainer;
    }

    @Bean
    public VaultContainer<?> vaultContainer() {
        return vaultContainer;
    }

    // Override properties using @DynamicPropertySource
    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Override database properties with testcontainer values
        registry.add("spring.datasource.hikari.jdbc-url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.hikari.username", postgresContainer::getUsername);
        registry.add("spring.datasource.hikari.password", postgresContainer::getPassword);

        // Override Flyway settings for tests
        registry.add("spring.flyway.clean-disabled", () -> "false");
        registry.add("spring.flyway.locations", () -> "classpath:db/migration,classpath:db/test-data");

        // Override Kafka properties
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
    }
}
