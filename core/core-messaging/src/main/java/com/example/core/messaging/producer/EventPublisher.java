package com.example.core.messaging.producer;

import java.util.concurrent.CompletableFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

@Component
public class EventPublisher {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventPublisher(KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void publishEvent(String topic, String key, Object event) {
        logger.info("Publishing event to topic: {} with key: {}", topic, key);

        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, event);

        future.whenComplete((result, exception) -> {
            if (exception == null) {
                logger.info(
                        "Event published successfully to topic: {} with key: {} at offset: {}",
                        topic,
                        key,
                        result.getRecordMetadata().offset());
            } else {
                logger.error("Failed to publish event to topic: {} with key: {}", topic, key, exception);
            }
        });
    }

    public void publishEvent(String topic, Object event) {
        publishEvent(topic, null, event);
    }
}
