package com.in28minutes.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

/**
 * Service responsible for consuming messages from Kafka topic.
 */
@Service
public class KafkaConsumerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaConsumerService.class);

    /**
     * Kafka listener method to receive messages from the configured topic.
     *
     * @param record The Kafka ConsumerRecord containing key, value, offset, partition, etc.
     */
    @KafkaListener(topics = "message-topic", groupId = "in28-group")
    public void listen(ConsumerRecord<String, String> record) {
        log.info("Received message from Kafka: {}", record.value());
    }
}
