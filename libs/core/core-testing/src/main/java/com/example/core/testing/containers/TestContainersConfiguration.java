package com.example.core.testing.containers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.vault.VaultContainer;

@TestConfiguration(proxyBeanMethods = false)
public class TestContainersConfiguration {

    private static final Logger logger = LoggerFactory.getLogger(TestContainersConfiguration.class);

    @Bean
    @ServiceConnection
    public PostgreSQLContainer<?> postgresContainer() {
        return new PostgreSQLContainer<>(DockerImageName.parse("postgres:15-alpine"))
                .withDatabaseName("testdb")
                .withUsername("test")
                .withPassword("test")
                .withReuse(true);
    }

    @Bean
    @ServiceConnection
    public KafkaContainer kafkaContainer() {
        return new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0")).withReuse(true);
    }

    @Bean
    public VaultContainer<?> vaultContainer() {
        return new VaultContainer<>(DockerImageName.parse("hashicorp/vault:latest"))
                .withVaultToken("test-token")
                .withVaultPort(8200)
                .withSecretInVault("secret/application", "spring.datasource.password", "test")
                .withReuse(true);
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        logger.debug(">>> Registering DynamicPropertySource properties");
    }
}
