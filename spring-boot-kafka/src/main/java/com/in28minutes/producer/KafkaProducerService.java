package com.in28minutes.producer;

import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Service responsible for producing messages to a Kafka topic.
 */
@Service
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${app.topic.name}")
    private String topicName;

    /**
     * Constructor to inject KafkaTemplate.
     *
     * @param kafkaTemplate KafkaTemplate to publish messages
     */
    public KafkaProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * Sends a message to the configured Kafka topic.
     * Uses CompletableFuture to handle asynchronous response and logs success/failure.
     *
     * @param message The message content to send
     */
    public void sendMessage(String message) {
        log.info("Producing message to Kafka topic [{}]: {}", topicName, message);

        CompletableFuture<SendResult<String, String>> future = kafkaTemplate.send(topicName, message);

        future.whenComplete((result, exception) -> {
            if (exception != null) {
                log.error("Failed to send message [{}] to topic [{}]", message, topicName, exception);
            } else {
                var metadata = result.getRecordMetadata();
                log.info("Message [{}] sent successfully to topic [{}], partition [{}], offset [{}]",
                        message, topicName, metadata.partition(), metadata.offset());
            }
        });
    }
}
