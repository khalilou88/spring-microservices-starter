package com.example.core.messaging.event;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.util.UUID;

public abstract class BaseEvent {

    @JsonProperty("eventId")
    private final String eventId = UUID.randomUUID().toString();

    @JsonProperty("eventType")
    private final String eventType = this.getClass().getSimpleName();

    @JsonProperty("timestamp")
    private final Instant timestamp = Instant.now();

    @JsonProperty("version")
    private final String version = "1.0";

    public String getEventId() { return eventId; }
    public String getEventType() { return eventType; }
    public Instant getTimestamp() { return timestamp; }
    public String getVersion() { return version; }
}