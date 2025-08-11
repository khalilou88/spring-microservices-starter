package com.example.core.events;

import java.util.List;

public interface EventStore {

    void saveEvents(String aggregateId, List<DomainEvent> events, Long expectedVersion);

    List<DomainEvent> getEvents(String aggregateId);

    List<DomainEvent> getEvents(String aggregateId, Long fromVersion);

    List<DomainEvent> getAllEvents();

    List<DomainEvent> getEventsByType(String eventType);
}
