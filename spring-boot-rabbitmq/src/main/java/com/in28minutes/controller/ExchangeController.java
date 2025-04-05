package com.in28minutes.controller;

import com.in28minutes.dto.Greetings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Demonstrates how to send messages using Fanout and Topic exchanges.
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Exchange Controller", description = "Handles sending messages to Fanout and Topic exchanges")
public class ExchangeController {

    private static final Logger log = LoggerFactory.getLogger(ExchangeController.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.fanout-exchange}")
    private String fanoutExchange;

    @Value("${rabbitmq.topic-exchange}")
    private String topicExchange;

    public ExchangeController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Example:
     * curl -X POST http://localhost:9100/api/fanout \
     *      -H "Content-Type: application/json" \
     *      -d '{"message": "Broadcast to all"}'
     */
    @Operation(
            summary = "Send message to Fanout exchange",
            description = "Publishes a broadcast message to all queues bound to the fanout exchange"
    )
    @PostMapping("/fanout")
    public ResponseEntity<?> sendToFanout(@RequestBody Greetings greetings) {
        String message = greetings.message();
        log.info("Sending to fanout exchange: {}", message);
        rabbitTemplate.convertAndSend(fanoutExchange, "", message); // No routing key for fanout
        return ResponseEntity.ok(Map.of("info", "Sent to fanout exchange"));
    }

    /**
     * Example:
     * curl -X POST http://localhost:9100/api/topic?key=user.created \
     *      -H "Content-Type: application/json" \
     *      -d '{"message": "User created event"}'
     */
    @Operation(
            summary = "Send message to Topic exchange",
            description = "Publishes a message to a topic exchange using a dynamic routing key"
    )
    @PostMapping("/topic")
    public ResponseEntity<?> sendToTopic(
            @Parameter(description = "Routing key pattern (e.g. user.created, order.shipped)")
            @RequestParam String key,
            @RequestBody Greetings greetings) {
        String msg = greetings.message();
        log.info("Sending to topic exchange with key [{}]: {}", key, msg);
        rabbitTemplate.convertAndSend(topicExchange, key, msg);
        return ResponseEntity.ok(Map.of("info", "Sent to topic exchange with routing key: " + key));
    }
}
