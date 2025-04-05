package com.in28minutes.controller;

import com.in28minutes.dto.Greetings;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * RabbitTemplate is the core class provided by Spring AMQP for publishing messages to RabbitMQ.
 * Official Docs: <a href="https://docs.spring.io/spring-amqp/reference/amqp/template.html">...</a>
 */
@RestController
@RequestMapping("/api")
@Tag(name = "Producer Controller", description = "Sends messages to RabbitMQ using Direct Exchange")
public class ProducerController {

    private static final Logger log = LoggerFactory.getLogger(ProducerController.class);

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    public ProducerController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    /**
     * Example curl:
     * curl -X POST http://localhost:9100/api/produce \
     * -H "Content-Type: application/json" \
     * -d '{"message": "Hello, RabbitMQ!"}'
     */
    @Operation(
            summary = "Send message to RabbitMQ queue",
            description = "Publishes a message to RabbitMQ via direct exchange using configured routing key"
    )
    @PostMapping("/produce")
    public ResponseEntity<?> sendMessage(@RequestBody Greetings greetings) {
        String message = greetings.message();
        log.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message,
                messageToQueue -> {
                    messageToQueue.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                    return messageToQueue;
                });
        log.info("Message sent successfully to RabbitMQ (via Direct Exchange)");

        return ResponseEntity.accepted().body(Map.of(
                "statusCode", 202,
                "info", "Acknowledged"
        ));
    }
}
