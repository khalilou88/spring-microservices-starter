package com.example.userapi;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = {"com.example"})
@EnableConfigurationProperties
public class TestApplication {
    // This class is only used for testing
    // It provides the @SpringBootConfiguration needed by tests
}
