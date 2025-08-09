package com.example.core.monitoring.health;

import org.springframework.boot.actuator.health.Health;
import org.springframework.boot.actuator.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component("custom")
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        // Add custom health check logic here
        boolean serviceIsUp = checkExternalService();

        if (serviceIsUp) {
            return Health.up()
                    .withDetail("service", "Available")
                    .withDetail("version", "1.0.0")
                    .build();
        } else {
            return Health.down()
                    .withDetail("service", "Unavailable")
                    .withDetail("error", "External service is down")
                    .build();
        }
    }

    private boolean checkExternalService() {
        // Implement your health check logic
        return true;
    }
}
