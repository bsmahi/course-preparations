package com.in28minutes.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents a greeting message for RabbitMQ producers.
 * Java Records automatically generate constructor, getters, equals, hashCode, and toString.
 */
@Schema(description = "Data Transfer Object for greeting messages used in producer and exchange endpoints")
public record Greetings(
        @Schema(
                description = "The message content to be sent to RabbitMQ",
                example = "Hello, RabbitMQ!"
        )
        String message
) {}
