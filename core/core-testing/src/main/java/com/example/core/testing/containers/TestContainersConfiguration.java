package com.example.core.testing.containers;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.vault.core.VaultTemplate;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.vault.VaultContainer;

@TestConfiguration(proxyBeanMethods = false)
@Profile("test")
@Testcontainers
public class TestContainersConfiguration {

    // Static containers for @DynamicPropertySource access
    @Container
    static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>(
                    DockerImageName.parse("postgres:15-alpine"))
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test")
            .withReuse(true);

    @Container
    static KafkaContainer kafkaContainer =
            new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0")).withReuse(true);

    @Container
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
        // Database configuration
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.datasource.driver-class-name", () -> "org.postgresql.Driver");

        // Connection pool settings for tests
        registry.add("spring.datasource.hikari.maximum-pool-size", () -> "2");
        registry.add("spring.datasource.hikari.minimum-idle", () -> "1");
        registry.add("spring.datasource.hikari.connection-timeout", () -> "10000");
        registry.add("spring.datasource.hikari.validation-timeout", () -> "5000");

        // Override Kafka properties
        registry.add("spring.kafka.bootstrap-servers", kafkaContainer::getBootstrapServers);

        // Vault properties
        registry.add("spring.cloud.vault.host", vaultContainer::getHost);
        registry.add("spring.cloud.vault.port", () -> vaultContainer.getMappedPort(8200));
        registry.add("spring.cloud.vault.token", () -> "test-token"); // Use the token we set in withVaultToken()

        // Let JPA handle schema creation for tests
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "create-drop");
        registry.add("spring.jpa.show-sql", () -> "true");
    }

    // Create mock VaultTemplate bean for tests (simpler approach)
    @Bean
    @Primary
    public VaultTemplate vaultTemplate() {
        return Mockito.mock(VaultTemplate.class);
    }
}
