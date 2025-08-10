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
    @Bean
    public PostgreSQLContainer<?> postgresContainer() {
        return postgresContainer;
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
        // PostgreSQL properties
        registry.add("spring.datasource.hikari.jdbc-url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.hikari.username", postgresContainer::getUsername);
        registry.add("spring.datasource.hikari.password", postgresContainer::getPassword);
        registry.add("spring.datasource.hikari.driver-class-name", postgresContainer::getDriverClassName);

        // Kafka properties
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);
        registry.add(
                "spring.kafka.producer.key-serializer", () -> "org.apache.kafka.common.serialization.StringSerializer");
        registry.add(
                "spring.kafka.producer.value-serializer",
                () -> "org.springframework.kafka.support.serializer.JsonSerializer");
        registry.add(
                "spring.kafka.consumer.key-deserializer",
                () -> "org.apache.kafka.common.serialization.StringDeserializer");
        registry.add(
                "spring.kafka.consumer.value-deserializer",
                () -> "org.springframework.kafka.support.serializer.JsonDeserializer");
        registry.add("spring.kafka.consumer.group-id", () -> "test-group");
        registry.add("spring.kafka.consumer.auto-offset-reset", () -> "earliest");

        // Vault properties
        //        registry.add("spring.cloud.vault.enabled", () -> "true");
        //        registry.add("spring.cloud.vault.host", vaultContainer::getHost);
        //        registry.add("spring.cloud.vault.port", () -> vaultContainer.getMappedPort(8200));
        //        registry.add("spring.cloud.vault.scheme", () -> "http");
        //        registry.add("spring.cloud.vault.token", () -> "test-token");
        //        registry.add("spring.cloud.vault.kv.enabled", () -> "true");
        //        registry.add("spring.cloud.vault.kv.backend", () -> "secret");
        //        registry.add("spring.cloud.vault.kv.default-context", () -> "application");
    }
}
