package com.example.core.monitoring.metrics;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.stereotype.Component;

@Component
public class BusinessMetrics {

    private final Counter userCreatedCounter;
    private final Counter userDeletedCounter;
    private final Timer userOperationTimer;
    private final MeterRegistry meterRegistry;

    public BusinessMetrics(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        this.userCreatedCounter = Counter.builder("users.created")
                .description("Number of users created")
                .register(meterRegistry);

        this.userDeletedCounter = Counter.builder("users.deleted")
                .description("Number of users deleted")
                .register(meterRegistry);

        this.userOperationTimer = Timer.builder("users.operation.duration")
                .description("Time spent on user operations")
                .register(meterRegistry);
    }

    public void incrementUserCreated() {
        userCreatedCounter.increment();
    }

    public void incrementUserDeleted() {
        userDeletedCounter.increment();
    }

    public Timer.Sample startTimer() {
        return Timer.start(meterRegistry);
    }

    public void recordUserOperation(Timer.Sample sample) {
        sample.stop(userOperationTimer);
    }
}
