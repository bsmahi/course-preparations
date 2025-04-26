package com.in28minutes.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Kafka configuration for creating a topic on application startup.
 */
@Configuration
public class KafkaConfig {

    public static final String TOPIC_NAME = "demo-topic";

    @Bean
    public NewTopic topic() {
        return new NewTopic(TOPIC_NAME, 1, (short) 1); // 1 partition, 1 replica
    }
}
