package com.example.core.events;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private final ApplicationEventPublisher applicationEventPublisher;

    public EventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    public void publishEvent(DomainEvent event) {
        logger.info("Publishing domain event: {} for aggregate: {}", event.getEventType(), event.getAggregateId());
        applicationEventPublisher.publishEvent(event);
    }

    public void publishEvents(List<DomainEvent> events) {
        events.forEach(this::publishEvent);
    }
}
