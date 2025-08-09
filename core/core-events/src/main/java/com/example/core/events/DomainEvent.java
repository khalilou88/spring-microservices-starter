package com.example.core.events;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;

public abstract class DomainEvent {

    @JsonProperty("eventId")
    private final String eventId;

    @JsonProperty("aggregateId")
    private final String aggregateId;

    @JsonProperty("aggregateType")
    private final String aggregateType;

    @JsonProperty("eventType")
    private final String eventType;

    @JsonProperty("version")
    private final Long version;

    @JsonProperty("occurredAt")
    private final Instant occurredAt;

    protected DomainEvent(String aggregateId, String aggregateType, Long version) {
        this.eventId = UUID.randomUUID().toString();
        this.aggregateId = aggregateId;
        this.aggregateType = aggregateType;
        this.eventType = this.getClass().getSimpleName();
        this.version = version;
        this.occurredAt = Instant.now();
    }

    // Getters
    public String getEventId() {
        return eventId;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public String getAggregateType() {
        return aggregateType;
    }

    public String getEventType() {
        return eventType;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getOccurredAt() {
        return occurredAt;
    }
}
