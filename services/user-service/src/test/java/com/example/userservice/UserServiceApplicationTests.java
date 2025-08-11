package com.example.userservice;

import com.example.core.testing.annotation.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;

@IntegrationTest
class UserServiceApplicationTests {

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        System.out.println(">>> Registering DynamicPropertySource properties UserServiceApplicationTests");
    }

    @Test
    void contextLoads() {
        // Test that Spring context loads successfully
    }
}
